package com.example.kersach;

public class Water {
    public DMY c_lastDate;
    public DMY h_lastDate;

    public int c_lastPokaz, c_dolg, c_lastCost;
    public int h_lastPokaz, h_dolg, h_lastCost;

    public Water() {

    }
    
    public Water(DMY c_lastDate, DMY h_lastDate, int c_lastPokaz, int c_dolg, int c_lastCost, int h_lastPokaz, int h_dolg, int h_lastCost) {
        this.c_lastDate = c_lastDate;
        this.h_lastDate = h_lastDate;
        this.c_lastPokaz = c_lastPokaz;
        this.c_dolg = c_dolg;
        this.c_lastCost = c_lastCost;
        this.h_lastPokaz = h_lastPokaz;
        this.h_dolg = h_dolg;
        this.h_lastCost = h_lastCost;
    }

    public DMY getC_lastDate() {
        return c_lastDate;
    }

    public void setC_lastDate(DMY c_lastDate) {
        this.c_lastDate = c_lastDate;
    }

    public DMY getH_lastDate() {
        return h_lastDate;
    }

    public void setH_lastDate(DMY h_lastDate) {
        this.h_lastDate = h_lastDate;
    }

    public int getC_lastPokaz() {
        return c_lastPokaz;
    }

    public void setC_lastPokaz(int c_lastPokaz) {
        this.c_lastPokaz = c_lastPokaz;
    }

    public int getC_dolg() {
        return c_dolg;
    }

    public void setC_dolg(int c_dolg) {
        this.c_dolg = c_dolg;
    }

    public int getC_lastCost() {
        return c_lastCost;
    }

    public void setC_lastCost(int c_lastCost) {
        this.c_lastCost = c_lastCost;
    }

    public int getH_lastPokaz() {
        return h_lastPokaz;
    }

    public void setH_lastPokaz(int h_lastPokaz) {
        this.h_lastPokaz = h_lastPokaz;
    }

    public int getH_dolg() {
        return h_dolg;
    }

    public void setH_dolg(int h_dolg) {
        this.h_dolg = h_dolg;
    }

    public int getH_lastCost() {
        return h_lastCost;
    }

    public void setH_lastCost(int h_lastCost) {
        this.h_lastCost = h_lastCost;
    }
}
