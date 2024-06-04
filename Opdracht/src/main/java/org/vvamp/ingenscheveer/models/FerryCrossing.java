package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.models.json.AisSignal;

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

    @JsonProperty("AIS Signals")
    private ArrayList<AisSignal> aisSignals;


    public FerryCrossing(StatusUpdate departure, StatusUpdate arrival, ArrayList<AisSignal> aisSignals) {
        this.departure = departure;
        this.arrival = arrival;
        this.aisSignals = aisSignals;
    }
    public FerryCrossing(StatusUpdate departure, ArrayList<AisSignal> aisSignals) {
        this.departure = departure;
        this.arrival = null;
        this.aisSignals = aisSignals;
    }
    public FerryCrossing(StatusUpdate departure) {
        this.departure = departure;
        this.arrival = null;
        this.aisSignals = new ArrayList<AisSignal>();
    }

    public StatusUpdate getDeparture() {
        return departure;
    }

    public StatusUpdate getArrival() {
        return arrival;
    }

    public ArrayList<AisSignal> getAisSignals() {
        return aisSignals;
    }

    public boolean isActive(){
        return arrival == null;
    }

    public void setArrival(StatusUpdate arrival) {
        this.arrival = arrival;
    }

    public void setAisSignals(ArrayList<AisSignal> aisSignals) {
        this.aisSignals = aisSignals;
    }

    public void addAisSignal(AisSignal aisSignal) {
        this.aisSignals.add(aisSignal);
    }
}
