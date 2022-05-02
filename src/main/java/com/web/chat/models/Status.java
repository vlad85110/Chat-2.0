package com.web.chat.models;

public class Status {
    private String online;
    private String offline;

    public String getOnline() {
        return online;
    }

    public String getOffline() {
        return offline;
    }

    public Status(String online, String offline) {
        this.online = online;
        this.offline = offline;
    }
}
