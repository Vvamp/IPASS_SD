package org.vvamp.ingenscheveer.models;

import org.vvamp.ingenscheveer.CrossingController;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.database.models.AisData;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.util.ArrayList;
import java.util.List;

public class Ferry {


    private String name;
    private static Ferry ferry = new Ferry("Geldersweert");


    private List<FerryCrossing> ferryCrossings = new ArrayList<>();

    public Ferry(String name) {

//        ArrayList<AisSignal> aisSignals = Main.storageController.load();
        List<AisData> aisSignals = DatabaseStorageController.getDatabaseAisController().getAllAisData();
        CrossingController crossingController = new CrossingController();
        List<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(aisSignals);
        ferryCrossings = crossingController.getFerryCrossings(statusUpdates);
        this.name = name;
    }

    public static Ferry getFerry() {
        return ferry;
    }

    public String getName() {
        return name;
    }


    public List<FerryCrossing> getFerryCrossings() {
        return getFerryCrossings(1440);
    }

        public List<FerryCrossing> getFerryCrossings(int aisLimit) {
//        List<AisSignal> aisSignals = DatabaseStorageController.getDatabaseAisController().getXMostRecentSignals(aisLimit);
        List<AisData> aisData =DatabaseStorageController.getDatabaseAisController().getAllAisData();
        CrossingController crossingController = new CrossingController();
        List<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(aisData);
        ferryCrossings = crossingController.getFerryCrossings(statusUpdates);
        return ferryCrossings;
    }

    public void setFerryCrossings(List<FerryCrossing> ferryCrossings) {
        this.ferryCrossings = ferryCrossings;
    }


}
