package com.example.messageservicesender.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String message;
    private int emailResponseCode;

    public Message() {
    }

    public Message(Long id, String sender, String message, int emailResponseCode) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.emailResponseCode = emailResponseCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getEmailResponseCode() {
        return emailResponseCode;
    }

    public void setEmailResponseCode(int emailResponseCode) {
        this.emailResponseCode = emailResponseCode;
    }
}
