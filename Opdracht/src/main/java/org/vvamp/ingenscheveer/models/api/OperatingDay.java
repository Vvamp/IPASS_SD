package org.vvamp.ingenscheveer.models.api;

import java.time.LocalTime;

public class OperatingDay {
    private String day;
    private LocalTime openingTime;

    public LocalTime getClosingTime() {
        return closingTime;
    }

    public LocalTime getOpeningTime() {
        return openingTime;
    }

    public String getDay() {
        return day;
    }

    private LocalTime closingTime;

    public OperatingDay(String day, LocalTime openingTime, LocalTime closingTime) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
}
