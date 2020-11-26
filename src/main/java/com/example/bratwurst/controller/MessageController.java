package com.example.bratwurst.controller;

import com.example.bratwurst.model.Message;
import com.example.bratwurst.service.FileService;
import com.example.bratwurst.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(value = "/api/messages")
public class MessageController
{
    @Autowired
    private MessageService messageService;

    @Autowired
    private FileService fileService;

    @GetMapping("/{sender}/{receiver}")
    public List<Message> getConversation(@PathVariable int sender, @PathVariable int receiver)
    {
        return messageService.getConversation(sender, receiver);
    }

    @PostMapping("/new")
    public ResponseEntity postMessage(@RequestBody Message message)
    {
        messageService.postMessage(message);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/new/file/{sender}/{receiver}")
    public ResponseEntity uploadFile(@PathVariable int sender, @PathVariable int receiver, @RequestParam MultipartFile file)
    {
        fileService.saveFileWithMessage(sender, receiver, file);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("delete/file/{sender}/{receiver}/{filename}")
    public ResponseEntity deleteFile(@PathVariable int sender, @PathVariable int receiver, @RequestParam String filename){
        fileService.deleteFile(sender, receiver, filename);
        return new ResponseEntity(HttpStatus.OK);
    }
}
