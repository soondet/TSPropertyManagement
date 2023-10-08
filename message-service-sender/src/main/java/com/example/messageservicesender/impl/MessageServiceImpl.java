package com.example.messageservicesender.impl;

import com.example.messageservicesender.entity.Message;
import com.example.messageservicesender.repository.MessageRepository;
import com.example.messageservicesender.service.MessageService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public List<Message> list() {
        return messageRepository.findAll();
    }
}
