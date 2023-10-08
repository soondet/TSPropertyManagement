package com.example.messageservicesender.controller;

import com.example.messageservicesender.entity.Message;
import com.example.messageservicesender.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<Message>> list() {
        return ResponseEntity.ok(messageService.list());
    }
}
