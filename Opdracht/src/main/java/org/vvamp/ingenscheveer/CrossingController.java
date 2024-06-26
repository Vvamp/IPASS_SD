package org.vvamp.ingenscheveer;

import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.StatusUpdate;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.util.ArrayList;
import java.util.List;

public class CrossingController {
    public ArrayList<StatusUpdate> getStatusUpdates(ArrayList<AisSignal> aisSignals) {
        ArrayList<StatusUpdate> statusUpdates = new ArrayList<>();
        for (AisSignal message : aisSignals) {
//            Location direction = Location.UNKNOWN;
//
//
//            if(previousMessage != null) {
//                if (message.message.positionReport.latitude > previousMessage.message.positionReport.latitude) {
//                    direction=Location.ELST;
//                }else{
//                    direction=Location.INGEN;
//                }
//            }
            Location location = message.message.positionReport.latitude > 51.98 ? Location.ELST : Location.INGEN;
            StatusUpdate statusUpdate = new StatusUpdate(location,message);
            if(statusUpdates.size() == 0 || statusUpdates.get(statusUpdates.size()-1) != statusUpdate) {
                statusUpdates.add(statusUpdate);
            }
        }
        return statusUpdates;
    }

    public ArrayList<FerryCrossing> getFerryCrossings(ArrayList<StatusUpdate> statusUpdates) {
        ArrayList<FerryCrossing> ferryCrossings = new ArrayList<>();
        StatusUpdate lastUpdate = null;
        boolean wasSailing = false;
        for(StatusUpdate statusUpdate : statusUpdates) {
            if(statusUpdate.isSailing() && wasSailing == false){
                if(lastUpdate != null) {
                    FerryCrossing ferryCrossing = new FerryCrossing(statusUpdate);
                    ferryCrossings.add(ferryCrossing);
                }
            }else{
                if(ferryCrossings.size() > 0) {
                    ferryCrossings.get(ferryCrossings.size()-1).setArrival(statusUpdate);
                }
            }
            if(ferryCrossings.size() > 0) {
                ferryCrossings.get(ferryCrossings.size() - 1).addAisSignal(statusUpdate.getAisSignal());
            }

            wasSailing = statusUpdate.isSailing();
            lastUpdate = statusUpdate;

//            if(lastUpdate == null){
//                lastUpdate = statusUpdate;
//                FerryCrossing ferryCrossing = new FerryCrossing(statusUpdate);
//                ferryCrossings.add(ferryCrossing);
//                continue;
//            }
//
//
//            lastUpdate = statusUpdate;
        }

        return ferryCrossings;

    }
}
