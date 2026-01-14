package com.project.realhealthbuddy.Model;

public class HealthSummaryItem {

    public static final String TYPE_BMI = "BMI";
    public static final String TYPE_STEPS = "STEPS";
    public static final String TYPE_SLEEP = "SLEEP";
    public static final String TYPE_WATER = "WATER";
    public static final String TYPE_MED = "MED";

    public int icon;
    public String title;
    public String value;
    public String type;

    public HealthSummaryItem(int icon, String title, String value, String type) {
        this.icon = icon;
        this.title = title;
        this.value = value;
        this.type = type;
    }
}
