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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service("UploadService")
public class UploadServiceImpl implements UploadService {

    Logger log = Logger.getLogger(UploadServiceImpl.class.getName());

    @Autowired
    UploadRepo uploadRepo;

    @Autowired
    SanitizingService sanitizingService;

    private static final String UPLOADED_FOLDER = "src//main//resources//static//upload//";
    private static final String PROFILE_PICTURE_FOLDER = "src//main//resources//static//profilePictures//";
    private static final List<String> CONTENT_TYPES = Arrays.asList("image/png", "image/jpeg", "image/gif");

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

    @Override
    public String getProfilePicture(int id){
        return uploadRepo.getProfilePicture(id);
    }

    @Override
    public boolean verifyProfilePicture(MultipartFile file){
        String fileContentType = file.getContentType();
        if(CONTENT_TYPES.contains(fileContentType)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean uploadProfilePicture(MultipartFile file, int id) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        try {
            byte[] bytes = file.getBytes();
            String oldFilename = sanitizingService.sanitizeFilename(file.getOriginalFilename());
            String fileExtension = oldFilename.substring(oldFilename.lastIndexOf(".")+1);

            String newFilename = timestamp.toString();
            newFilename = newFilename.replace("-", "");
            newFilename = newFilename.replace(" ", "");
            newFilename = newFilename.replace(":", "");
            newFilename = newFilename.replace(".", "");

            String filename = newFilename + "." + fileExtension;

            Path path = Paths.get(PROFILE_PICTURE_FOLDER, filename);
            Files.write(path, bytes);

            if(uploadRepo.getProfilePicture(id) != null){
                deleteProfilePicture(uploadRepo.getProfilePicture(id));
            }

            log.info("Filename from DB: "+uploadRepo.getProfilePicture(id));

            log.info("File extension is: " + filename.substring(filename.lastIndexOf(".")+1));

            uploadRepo.saveProfilePicture(filename, id);

            log.info("Uploading profile picture: " + filename);

            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean deleteProfilePicture(String filename) {

        log.info("Delete profile picture: " + filename);

        try {
            Path file = Paths.get(PROFILE_PICTURE_FOLDER + filename);
            Files.delete(file);
            return true;
        } catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }
}
