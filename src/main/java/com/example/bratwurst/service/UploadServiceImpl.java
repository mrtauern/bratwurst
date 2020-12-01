package com.example.bratwurst.service;

import com.example.bratwurst.controller.UploadController;
import com.example.bratwurst.model.File;
import com.example.bratwurst.repo.UploadRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;

@Service("UploadService")
public class UploadServiceImpl implements UploadService {

    Logger log = Logger.getLogger(UploadServiceImpl.class.getName());

    @Autowired
    UploadRepo uploadRepo;

    private static final String UPLOADED_FOLDER = "src//main//resources//static//upload//";

    @Override
    public boolean uploadFile(MultipartFile file) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER, file.getOriginalFilename());
            Files.write(path, bytes);

            File newfile = new File(file.getOriginalFilename(), 1);

            uploadRepo.saveFile(newfile);

            log.info("Uploading: " + file.getOriginalFilename());

            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<File> getFiles(int user) {
        return uploadRepo.getFiles(user);
    }

    @Override
    public File getFile(int user) {
        return uploadRepo.getFile(user);
    }

    @Override
    public Boolean deleteFile(int id) {
        String filename = uploadRepo.deleteFile(id);

        try {
            Path file = Paths.get(UPLOADED_FOLDER + filename);
            Files.delete(file);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
