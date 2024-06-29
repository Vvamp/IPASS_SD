package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Drukte {
    private int severity;
    private LocalDateTime time;
    private static ArrayList<Drukte> druktes = new ArrayList<>();

    public Drukte(int severity, LocalDateTime time) {
        this.severity = severity;
        this.time = time;
        this.druktes.add(this);
    }

    @JsonProperty("Severity")
    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    @JsonIgnore
    public LocalDateTime getTime() {
        return time;
    }
    @JsonProperty("Time")
    public String getTimestampString(){
        return time.toString();
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    @JsonIgnore
    public static ArrayList<Drukte> getAll(){
        return druktes;
    }
}
