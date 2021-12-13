package com.example.crappostsbackend.exception;

import java.util.Date;
import java.util.List;

public class ErrorDetails {
    private Date timeStamp;
    private List<String> messages;
    private String details;

    public ErrorDetails(Date timeStamp, List<String> messages, String details) {
        this.timeStamp = timeStamp;
        this.messages = messages;
        this.details = details;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
