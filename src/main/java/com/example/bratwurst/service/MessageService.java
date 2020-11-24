package com.example.bratwurst.service;

import com.example.bratwurst.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MessageService
{
    public List<Message> getConversation(int sender, int receiver);
    public void postMessage(Message msg);
}
