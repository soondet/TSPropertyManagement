package com.example.messageservicereceiver.service;

import com.example.messageservicereceiver.entity.Information;
import com.example.messageservicereceiver.entity.Message;

import java.util.List;

public interface MessageService {

    void sendMessage(Information information);

    List<Message> listBySender(String sender);

    List<Message> listLastTen();
}
