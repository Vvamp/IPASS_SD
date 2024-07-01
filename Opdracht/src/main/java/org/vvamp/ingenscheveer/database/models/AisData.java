package org.vvamp.ingenscheveer.database.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class AisData {
    private Timestamp timestamp;

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setSog(double sog) {
        this.sog = sog;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getRaw_json() {
        return raw_json;
    }

    public void setRaw_json(String raw_json) {
        this.raw_json = raw_json;
    }

    @JsonProperty("sog")
    private double sog;
    private double longitude;
    private double latitude;
    private String raw_json;

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

    @JsonIgnore
    public double getSog(){
        return sog;
    }
}
