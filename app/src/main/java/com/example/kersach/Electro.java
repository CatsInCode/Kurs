package com.example.kersach;

import java.util.Date;

public class Electro {
    public DMY lastDate;
    public int lastPokaz, dolg, lastCost;

    public Electro() {

    }

    public Electro(DMY lastDate, int lastPokaz, int dolg, int lastCost) {
        this.lastDate = lastDate;
        this.lastPokaz = lastPokaz;
        this.dolg = dolg;
        this.lastCost = lastCost;
    }

    public DMY getLastDate() {
        return lastDate;
    }

    public void setLastDate(DMY lastDate) {
        this.lastDate = lastDate;
    }

    public int getLastPokaz() {
        return lastPokaz;
    }

    public void setLastPokaz(int lastPokaz) {
        this.lastPokaz = lastPokaz;
    }

    public int getDolg() {
        return dolg;
    }

    public void setDolg(int dolg) {
        this.dolg = dolg;
    }

    public int getLastCost() {
        return lastCost;
    }

    public void setLastCost(int lastCost) {
        this.lastCost = lastCost;
    }
}
