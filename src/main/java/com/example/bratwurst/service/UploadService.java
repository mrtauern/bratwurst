package com.example.bratwurst.service;

import com.example.bratwurst.model.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service("UploadService")
public interface UploadService {

    public boolean uploadFile(MultipartFile file);
    public List<File> getFiles(int user);
    public File getFile(int id);
    public Boolean deleteFile(int id);
}
