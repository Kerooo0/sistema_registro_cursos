package com.example.sistemagestion;

public class User {
    private int id;
    private String nombre;
    private int edad;
    private String password;

    public User(int id, String nombre, int edad, String password) {
        this.id = id;
        this.nombre = nombre;
        this.edad = edad;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getPassword() {
        return password;
    }
}