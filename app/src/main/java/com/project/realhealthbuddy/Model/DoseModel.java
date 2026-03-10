package com.project.realhealthbuddy.Model;

public class DoseModel {

    String name;
    String time;
    boolean taken;

    public DoseModel(String name, String time, boolean taken) {
        this.name = name;
        this.time = time;
        this.taken = taken;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public boolean isTaken() {
        return taken;
    }
}