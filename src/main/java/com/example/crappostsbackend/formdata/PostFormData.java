package com.example.crappostsbackend.formdata;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class PostFormData implements Serializable {

    @NotEmpty(message = "Text cannot be empty!")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
