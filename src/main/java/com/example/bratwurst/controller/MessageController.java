package com.example.bratwurst.controller;

import com.example.bratwurst.model.Message;
import com.example.bratwurst.model.User;
import com.example.bratwurst.service.FileService;
import com.example.bratwurst.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/api/messages")
public class MessageController
{
    @Autowired
    private MessageService messageService;

    @Autowired
    private FileService fileService;

    @CrossOrigin()
    @GetMapping("/{sender}/{receiver}")
    public ResponseEntity<List<Message>> getConversation(@PathVariable int sender, @PathVariable int receiver, HttpSession session)
    {
        try
        {
            User user = (User) session.getAttribute("login");
            if(sender != user.getId() || user.getId() == receiver)
            {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);

            }
            return new ResponseEntity<>(messageService.getConversation(sender, receiver), HttpStatus.OK);
        }
        catch (NullPointerException e)
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin()
    @PostMapping("/new")
    public ResponseEntity postMessage(@RequestBody Message message, HttpSession session)
    {
        try
        {
            User user = (User) session.getAttribute("login");
            if (message.getSender() != user.getId() || user.getId() == message.getReceiver())
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            messageService.postMessage(message);

            return new ResponseEntity(HttpStatus.OK);
        }
        catch (NullPointerException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin()
    @PostMapping("/new/file/{sender}/{receiver}")
    public ResponseEntity uploadFile(@PathVariable int sender, @PathVariable int receiver, @RequestParam MultipartFile file, HttpSession session)
    {
        try
        {
            User user = (User) session.getAttribute("login");
            if (receiver != user.getId() && sender != user.getId())
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fileService.saveFileWithMessage(sender, receiver, file);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (NullPointerException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin()
    @PostMapping("delete/file/{sender}/{receiver}/{filename}")
    public ResponseEntity deleteFile(@PathVariable int sender, @PathVariable int receiver, @PathVariable String filename, HttpSession session){
        try
        {
            User user = (User) session.getAttribute("login");
            if (sender != user.getId() || user.getId() == receiver)
            {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            fileService.deleteFile(sender, receiver, filename);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (NullPointerException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
