package org.vvamp.ingenscheveer.models.api;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class OperatingSchedule {
    private static final List<OperatingDay> days = new ArrayList<>();


    public OperatingSchedule() {
        if (days.size() == 0) {
            days.add(new OperatingDay("monday", LocalTime.of(6, 0, 0), LocalTime.of(23, 0, 0)));
            days.add(new OperatingDay("tuesday", LocalTime.of(6, 0, 0), LocalTime.of(23, 0, 0)));
            days.add(new OperatingDay("wednesday", LocalTime.of(6, 0, 0), LocalTime.of(23, 0, 0)));
            days.add(new OperatingDay("thursday", LocalTime.of(6, 0, 0), LocalTime.of(23, 0, 0)));
            days.add(new OperatingDay("friday", LocalTime.of(6, 0, 0), LocalTime.of(23, 0, 0)));
            days.add(new OperatingDay("saturday", LocalTime.of(7, 0, 0), LocalTime.of(0, 0, 0)));
            days.add(new OperatingDay("sunday", LocalTime.of(9, 0, 0), LocalTime.of(23, 0, 0)));
        }
    }

    public List<OperatingDay> getDays() {
        return days;
    }
}
