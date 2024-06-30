package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.Location;
import org.vvamp.ingenscheveer.database.models.AisData;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class StatusUpdate {
    @JsonProperty("Location")
    private Location location;

    @JsonProperty("AIS Signal")
    private AisData aisSignal;

    public StatusUpdate(Location location, AisData aisSignal) {
        this.location = location;
        this.aisSignal = aisSignal;
    }

    public boolean isSailing() {
        return aisSignal.sog > 0.2;
    }

    public Location getLocation() {
        return location;
    }


    public int getEpochSeconds(){
        LocalDateTime dt = aisSignal.timestamp.toLocalDateTime();
        return (int) dt.toEpochSecond(ZoneOffset.UTC);
    }
    @JsonIgnore
    public AisData getAisData() {
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
