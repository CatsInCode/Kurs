package com.example.kersach;

public class User {
    public String id, login;

    public Electro electro;

    public Water water;

    public User() {
    }

    public User(String id, String login, Electro electro, Water water) {
        this.id = id;
        this.login = login;
        this.electro = electro;
        this.water = water;
    }

    public Water getWater() {
        return water;
    }

    public void setWater(Water water) {
        this.water = water;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Electro getElectro() {
        return electro;
    }

    public void setElectro(Electro electro) {
        this.electro = electro;
    }
}
