package com.web.chat.models.users;

public class Person {
    private static int idGetter = 0;

    public void setId(int id) {
        this.id = id;
    }

    private int id;
    private String name;
    private int password;

    private String strPassword;

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    public Person() {
        id = idGetter++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.hashCode();
    }

    public void setPassword(int password) {
        this.password = password;
    }
}
