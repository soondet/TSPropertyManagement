package com.example.messageservicereceiver.controller;

import com.example.messageservicereceiver.entity.Information;
import com.example.messageservicereceiver.entity.Message;
import com.example.messageservicereceiver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value = "/send-message")
    public ResponseEntity<String> sendMessage(@RequestBody Information information) {
        messageService.sendMessage(information);
        return ResponseEntity.ok("Message sent successfully");
    }

    @GetMapping("/list")
    public ResponseEntity<List<Message>> list(@RequestParam(required = false) String sender) {
        return ResponseEntity.ok(sender != null
                ? messageService.listBySender(sender)
                : messageService.listLastTen());
    }

}
