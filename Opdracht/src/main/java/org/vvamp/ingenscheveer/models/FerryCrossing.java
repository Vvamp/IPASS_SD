package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.database.models.AisData;

import java.util.ArrayList;


public class FerryCrossing {
//    private Location departureLocation;
//    private Location arrivalLocation;
//    private LocalDateTime departureTime;
//    private LocalDateTime arrivalTime;
    @JsonProperty("Departure")
    private StatusUpdate departure;

    @JsonProperty("Arrival")
    private StatusUpdate arrival;

    private ArrayList<AisData> aisSignals;


    public FerryCrossing(StatusUpdate departure, StatusUpdate arrival, ArrayList<AisData> aisSignals) {
        this.departure = departure;
        this.arrival = arrival;
        this.aisSignals = aisSignals;
    }
    public FerryCrossing(StatusUpdate departure, ArrayList<AisData> aisSignals) {
        this.departure = departure;
        this.arrival = null;
        this.aisSignals = aisSignals;
    }
    public FerryCrossing(StatusUpdate departure) {
        this.departure = departure;
        this.arrival = null;
        this.aisSignals = new ArrayList<AisData>();
    }

    public StatusUpdate getDeparture() {
        return departure;
    }

    public void setAisSignals(ArrayList<AisData> aisSignals) {
        this.aisSignals = aisSignals;
    }

    public StatusUpdate getArrival() {
        return arrival;
    }

    @JsonProperty("AIS Signals")
    public ArrayList<AisData> getAisData() {
        return aisSignals;
    }

    public boolean isActive(){
        return arrival == null;
    }

    public void setArrival(StatusUpdate arrival) {
        this.arrival = arrival;
    }

    public void addAisDatas(ArrayList<AisData> aisSignals) {
        this.aisSignals = aisSignals;
    }

    public void addAisData(AisData aisSignal) {
        this.aisSignals.add(aisSignal);
    }
}
