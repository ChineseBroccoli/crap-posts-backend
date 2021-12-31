package com.example.crappostsbackend.formdata;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class PostFormData implements Serializable {

    private String title;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
