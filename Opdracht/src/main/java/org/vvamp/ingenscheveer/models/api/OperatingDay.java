package org.vvamp.ingenscheveer.models.api;

import java.time.LocalTime;

public class OperatingDay {
    public String day;
    public LocalTime openingTime;
    public LocalTime closingTime;

    public OperatingDay(String day, LocalTime openingTime, LocalTime closingTime) {
        this.day = day;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }
}
