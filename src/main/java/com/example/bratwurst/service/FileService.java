package com.example.bratwurst.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public interface FileService
{
    public Resource loadFile(int sender, int receiver, String filename);
    public String saveFile(String path, MultipartFile file);
    public void saveFileWithMessage(int sender, int receiver, MultipartFile file);
    public void deleteFile(int sender, int receiver, String filename);
}
