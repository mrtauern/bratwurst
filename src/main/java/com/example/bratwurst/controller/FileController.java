package com.example.bratwurst.controller;

import com.example.bratwurst.model.User;
import com.example.bratwurst.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/files")
public class FileController
{
    @Autowired
    FileService fileService;

    @GetMapping("/{sender}/{receiver}/{filename}")
    public ResponseEntity<Resource> serveFile(@PathVariable int sender, @PathVariable int receiver, @PathVariable String filename, HttpSession session)
    {
        try
        {
            User user = (User) session.getAttribute("login");

            if (receiver != user.getId() && sender != user.getId())
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Resource resource = fileService.loadFile(sender, receiver, filename);

            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename\"=" + resource.getFilename() + "\"").body(resource);
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    //upload profile picture

}
