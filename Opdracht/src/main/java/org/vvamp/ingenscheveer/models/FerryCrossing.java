package org.vvamp.ingenscheveer.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.vvamp.ingenscheveer.Location;
import org.vvamp.ingenscheveer.models.json.MainMessage;

import java.time.LocalDateTime;
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
    private ArrayList<MainMessage> aisSignals;


    public FerryCrossing(StatusUpdate departure, StatusUpdate arrival, ArrayList<MainMessage> aisSignals) {
        this.departure = departure;
        this.arrival = arrival;
        this.aisSignals = aisSignals;
    }
    public FerryCrossing(StatusUpdate departure, ArrayList<MainMessage> aisSignals) {
        this.departure = departure;
        this.arrival = null;
        this.aisSignals = aisSignals;
    }
    public FerryCrossing(StatusUpdate departure) {
        this.departure = departure;
        this.arrival = null;
        this.aisSignals = new ArrayList<MainMessage>();
    }

    public StatusUpdate getDeparture() {
        return departure;
    }

    public StatusUpdate getArrival() {
        return arrival;
    }

    public ArrayList<MainMessage> getAisSignals() {
        return aisSignals;
    }

    public boolean isActive(){
        return arrival == null;
    }

    public void setArrival(StatusUpdate arrival) {
        this.arrival = arrival;
    }

    public void setAisSignals(ArrayList<MainMessage> aisSignals) {
        this.aisSignals = aisSignals;
    }

    public void addAisSignal(MainMessage aisSignal) {
        this.aisSignals.add(aisSignal);
    }
}
