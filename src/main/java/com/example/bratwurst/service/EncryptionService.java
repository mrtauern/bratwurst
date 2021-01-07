package com.example.bratwurst.service;

import org.springframework.stereotype.Service;

@Service
public interface EncryptionService
{
    public String encrypt(String data);
    public String decrypt(String data);
}
