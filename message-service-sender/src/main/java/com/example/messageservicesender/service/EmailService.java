package com.example.messageservicesender.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendMessage(String to,String subject, String text);
}
