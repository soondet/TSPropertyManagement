package com.example.messageservicereceiver.impl;

import com.example.messageservicereceiver.entity.Information;
import com.example.messageservicereceiver.entity.Message;
import com.example.messageservicereceiver.repository.MessageRepository;
import com.example.messageservicereceiver.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final static String TOPIC_MESSAGE_SEND = "message.send";

    private final KafkaTemplate<String, Information> kafkaTemplate;

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(KafkaTemplate<String, Information> kafkaTemplate, MessageRepository messageRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
    }

    @Override
    public void sendMessage(Information information) {
        Message message = messageRepository.save(new Message(information.getSender(), information.getMessage()));
        kafkaTemplate.send(TOPIC_MESSAGE_SEND, new Information(message.getId(), message.getSender(), message.getMessage()));
    }

    @Override
    public List<Message> listBySender(String sender) {
        return messageRepository.findBySender(sender);
    }

    @Override
    public List<Message> listLastTen() {
        return messageRepository.findTop10ByOrderByIdDesc();
    }
}
