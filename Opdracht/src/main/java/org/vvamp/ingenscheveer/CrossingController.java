package org.vvamp.ingenscheveer;

import org.vvamp.ingenscheveer.database.models.AisData;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.StatusUpdate;

import java.util.ArrayList;
import java.util.List;

public class CrossingController {
    public List<StatusUpdate> getStatusUpdates(List<AisData> aisSignals) {
        List<StatusUpdate> statusUpdates = new ArrayList<>();
        for (AisData message : aisSignals) {
            int messagIndex = aisSignals.indexOf(message);

            Location location = message.getLatitude() > 51.98 ? Location.ELST : Location.INGEN;
            StatusUpdate statusUpdate = new StatusUpdate(location, message);
            // Add location if its the first or if the status since the previous update changed from sailing to not sailing or the location changed

            if (statusUpdates.size() == 0 || statusUpdates.get(statusUpdates.size() - 1) != statusUpdate) {
                statusUpdates.add(statusUpdate);
            }
        }
        return statusUpdates;
    }

    public List<FerryCrossing> getFerryCrossings(List<StatusUpdate> statusUpdates) {
        List<FerryCrossing> ferryCrossings = new ArrayList<>();
        StatusUpdate lastUpdate = null;
        boolean wasSailing = false;
        for (StatusUpdate statusUpdate : statusUpdates) {
            if (statusUpdate.isSailing() && !wasSailing) {
                if (lastUpdate != null) {
                    FerryCrossing ferryCrossing = new FerryCrossing(statusUpdate);
                    ferryCrossings.add(ferryCrossing);
                }
            } else if (!statusUpdate.isSailing() && wasSailing && ferryCrossings.size() > 0) {
                ferryCrossings.get(ferryCrossings.size() - 1).setArrival(statusUpdate);
            }

            if (ferryCrossings.size() > 0) {
                ferryCrossings.get(ferryCrossings.size() - 1).addAisData(statusUpdate.getAisData());
            }

            wasSailing = statusUpdate.isSailing();
            lastUpdate = statusUpdate;

        }

        return ferryCrossings;

    }
}
