package com.example.bratwurst.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import org.unbescape.html.HtmlEscape;

@Service
public class SanitizingServiceImpl implements SanitizingService
{
    @Override
    public String sanitizeString(String input)
    {
        return HtmlUtils.htmlEscape(input);
    }

    @Override
    public String sanitizeFilename(String input)
    {
        return input.replaceAll("[^\\p{IsDigit}\\p{IsAlphabetic}.]", "");
    }
}
