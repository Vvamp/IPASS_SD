package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Drukte {
    private int severity;
    private LocalDateTime time;

    public Drukte(int severity, LocalDateTime time) {
        this.severity = severity;
        this.time = time;
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
    public static List<Drukte> getAll(){
        return DatabaseStorageController.getDatabaseDrukteController().getAllDrukte();
    }
}
