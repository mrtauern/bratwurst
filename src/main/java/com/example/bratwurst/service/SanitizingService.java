package com.example.bratwurst.service;

import org.springframework.stereotype.Service;

@Service
public interface SanitizingService
{
    public String sanitizeString(String input);
    public String sanitizeFilename(String input);
}
