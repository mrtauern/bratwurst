package com.example.bratwurst.service;

import com.example.bratwurst.controller.HomeController;
import com.example.bratwurst.model.Message;
import com.example.bratwurst.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Service
public class FileServiceImpl implements FileService
{
    Logger log = Logger.getLogger(FileServiceImpl.class.getName());

    private final Path root;

    @Autowired
    MessageRepo messageRepo;

    @Autowired
    SanitizingService sanitizingService;

    public FileServiceImpl()
    {
        root = Paths.get("files");
        log.info(root.toAbsolutePath().toString());

        if(!root.toFile().exists())
        {
            if(!root.toFile().mkdirs())
            {
                throw new RuntimeException("Could not create folder for storing files");
            }
        }
    }

    @Override
    public Resource loadFile(int sender, int receiver, String filename)
    {
        try
        {
            Path file = root.resolve(sender + "/" + receiver + "/" + filename);
            Resource resource = new UrlResource(file.toUri());

            if(resource.isReadable() && resource.exists())
            {
                return resource;
            }
            else
            {
                log.info(file.toAbsolutePath().toString());
                log.info("read: " + resource.isReadable());
                log.info("exists: " + resource.exists());
                throw new RuntimeException("File is not readable");
            }
        }
        catch (MalformedURLException e)
        {
            log.info(e.getStackTrace().toString());
            throw new RuntimeException("File not found");
        }
    }

    @Override
    public String saveFile(String path, MultipartFile file)
    {
        try
        {
            String filename = sanitizingService.sanitizeFilename(file.getOriginalFilename());
            if(file.isEmpty())
            {
                throw new RuntimeException("File is empty");
            }

            File pathFile = new File(root.toString() + path);
            if(!pathFile.exists())
            {
                if(!pathFile.mkdirs())
                {
                    throw new RuntimeException("Could not create userdirectory");
                }
            }

            File f = new File(root.toString() + path + "/" + filename);

            if(!f.getParent().equals(root.toString() + path))
            {
                throw new RuntimeException("Cannot place files outside of directory");
            }

            try(InputStream inputStream = file.getInputStream())
            {
                Files.copy(inputStream, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }

            return "/api/files" + path + "/" + filename;
        }
        catch (IOException e)
        {
            log.info(e.getStackTrace().toString());
            throw new RuntimeException("Could not save file");
        }
    }

    @Override
    public void saveFileWithMessage(int sender, int receiver, MultipartFile file)
    {
        String userPath = "/" + sender + "/" + receiver;
        String fileUrl = saveFile(userPath, file);
        Message msg = new Message();
        msg.setContent(fileUrl);
        msg.setFile(true);
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setTimestamp(LocalDateTime.now(Clock.systemUTC()).toString());
        messageRepo.postMessage(msg);
    }
}
