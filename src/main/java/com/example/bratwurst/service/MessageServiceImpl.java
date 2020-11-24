package com.example.bratwurst.service;

import com.example.bratwurst.model.Message;
import com.example.bratwurst.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService
{
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private SanitizingService sanitizingService;

    @Override
    public List<Message> getConversation(int sender, int receiver)
    {
        //TODO check if the token belongs to either sender or receiver. If not then block


        return messageRepo.getConversation(sender, receiver);
    }

    @Override
    public void postMessage(Message msg)
    {
        //TODO check if token belongs to either sender or receiver

        String sanitizedMsg = sanitizingService.sanitizeString(msg.getContent());

        msg.setContent(sanitizedMsg);
        msg.setTimestamp(LocalDateTime.now(Clock.systemUTC()).toString());

        messageRepo.postMessage(msg);
    }
}
