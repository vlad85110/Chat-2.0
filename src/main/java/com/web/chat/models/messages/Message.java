package com.web.chat.models.messages;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String text;
    private long time;
    private int id;
    private String author;

    public void setTime(int time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Message(String text) {
        setTime();
        this.text = text;
    }

    public Message() {
       setTime();
    }

    public String getText() {
        return text;
    }

    public long getTime() {
        return time;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTime() {
        time = System.currentTimeMillis();
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
