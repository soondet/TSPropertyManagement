package com.example.messageservicesender.listener;

import com.example.messageservicesender.entity.Message;
import com.example.messageservicesender.entity.listener.Information;
import com.example.messageservicesender.repository.MessageRepository;
import com.example.messageservicesender.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    @Value("${email.address}")
    private String emailAddress;

    private final EmailService emailService;

    private final MessageRepository messageRepository;

    public MessageListener(EmailService emailService, MessageRepository messageRepository) {
        this.emailService = emailService;
        this.messageRepository = messageRepository;
    }

    @KafkaListener(topics = "message.send", groupId = "message-consumer-group", containerFactory = "informationListener")
    public ResponseEntity<String> consume(Information information) {
        try {
            emailService.sendMessage(emailAddress, "Tестовое задание", information.toString());
            messageRepository.save(new Message(information.getId(), information.getSender(), information.getMessage(), HttpStatus.OK.value()));
            return ResponseEntity.ok("Email sent successfully");
        } catch (MailException e) {
            messageRepository.save(new Message(information.getId(), information.getSender(), information.getMessage(), HttpStatus.BAD_REQUEST.value()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send email: " + e.getMessage());
        } catch (Exception e) {
            messageRepository.save(new Message(information.getId(), information.getSender(), information.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
        }
    }
}
