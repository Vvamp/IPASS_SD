package org.vvamp.ingenscheveer.database.models;

import java.sql.Timestamp;

public class AisData {
    public Timestamp timestamp;
    public double sog;
    public double longitude;
    public double latitude;
    public String raw_json;

    public AisData(Timestamp timestamp, double sog, double longitude, double latitude, String raw_json) {
        this.timestamp = timestamp;
        this.sog = sog;
        this.longitude = longitude;
        this.latitude = latitude;
        this.raw_json = raw_json;
    }

    public long getUtcTimestamp() {
        return timestamp.getTime();
    }
}
