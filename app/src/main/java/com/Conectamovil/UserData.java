package com.Conectamovil;

public class UserData {
    private String email;
    private String name;

    // Constructor vacío requerido para Firebase
    public UserData() {
    }

    public UserData(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
