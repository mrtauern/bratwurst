package com.example.bratwurst.repo;

import com.example.bratwurst.model.File;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Repository("UploadRepo")
public interface UploadRepo {

    public File saveFile(File file);
    public List<File> getFiles(int user);
    public File getFile(int id);
    public String deleteFile(int id);
}
