package com.project.realhealthbuddy.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Medicine implements Serializable {

    private String id;
    private String name;
    private String dosage;
    private List<String> timings;
    private List<Boolean> takenStatus;
    private String startDate;
    private String color;

    public Medicine(String id, String name, String dosage,
                    List<String> timings, String startDate, String color) {
        this.id        = id;
        this.name      = name;
        this.dosage    = dosage;
        this.timings   = timings;
        this.startDate = startDate;
        this.color     = color;
        this.takenStatus = new ArrayList<>();
        for (int i = 0; i < timings.size(); i++) takenStatus.add(false);
    }

    public String getId()                       { return id; }
    public String getName()                     { return name; }
    public String getDosage()                   { return dosage; }
    public List<String> getTimings()            { return timings; }
    public List<Boolean> getTakenStatus()       { return takenStatus; }
    public String getStartDate()                { return startDate; }
    public String getColor()                    { return color; }
    public void setTakenStatus(List<Boolean> s) { this.takenStatus = s; }

    public int getTakenCount() {
        int c = 0;
        for (Boolean b : takenStatus) if (b) c++;
        return c;
    }

    public int getTotalDoses()      { return timings.size(); }

    public int getProgressPercent() {
        if (timings.isEmpty()) return 0;
        return getTakenCount() * 100 / getTotalDoses();
    }
}