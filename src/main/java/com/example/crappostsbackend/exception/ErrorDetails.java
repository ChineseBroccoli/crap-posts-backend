package com.example.crappostsbackend.exception;

import java.util.Date;
import java.util.Map;

public class ErrorDetails {
    private Date timeStamp;
    private Map<String, String> errorMessages;
    private String details;

    public ErrorDetails(Date timeStamp, Map<String, String> errorMessages, String details) {
        this.timeStamp = timeStamp;
        this.errorMessages = errorMessages;
        this.details = details;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Map<String, String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
