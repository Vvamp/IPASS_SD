package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.Location;
import org.vvamp.ingenscheveer.models.json.MainMessage;

import java.time.LocalDateTime;

public class StatusUpdate {
    @JsonProperty("Location")
    private Location location;

    @JsonProperty("AIS Signal")
    private MainMessage aisSignal;

    public StatusUpdate(Location location, MainMessage aisSignal) {
        this.location = location;
        this.aisSignal = aisSignal;
    }

    public boolean isSailing() {
        return aisSignal.message.positionReport.sog > 0.3;
    }

    public Location getLocation() {
        return location;
    }

    public MainMessage getAisSignal() {
        return aisSignal;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof StatusUpdate)) {
            return false;
        }
        StatusUpdate other = (StatusUpdate) o;
        return (isSailing() == other.isSailing() && getLocation() == other.getLocation());
    }
}
