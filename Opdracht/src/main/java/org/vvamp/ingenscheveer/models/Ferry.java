package org.vvamp.ingenscheveer.models;

import org.vvamp.ingenscheveer.CrossingController;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.util.ArrayList;
import java.util.List;

public class Ferry {


    private String name;
    private static Ferry ferry = new Ferry("Geldersweert");


    private List<FerryCrossing> ferryCrossings = new ArrayList<>();

    public Ferry(String name) {

//        ArrayList<AisSignal> aisSignals = Main.storageController.load();
        List<AisSignal> aisSignals = DatabaseStorageController.getDatabaseAisController().getAllAisSignals();
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
        List<AisSignal> aisSignals = DatabaseStorageController.getDatabaseAisController().getAllAisSignals();
        CrossingController crossingController = new CrossingController();
        List<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(aisSignals);
        ferryCrossings = crossingController.getFerryCrossings(statusUpdates);
        return ferryCrossings;
    }

    public void setFerryCrossings(List<FerryCrossing> ferryCrossings) {
        this.ferryCrossings = ferryCrossings;
    }


}
