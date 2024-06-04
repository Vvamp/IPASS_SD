package org.vvamp.ingenscheveer.models;

import org.vvamp.ingenscheveer.CrossingController;
import org.vvamp.ingenscheveer.LocalFileStorageController;
import org.vvamp.ingenscheveer.Main;
import org.vvamp.ingenscheveer.StorageController;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Ferry {


    private String name;
    private static Ferry ferry = new Ferry("Geldersweert");



    private ArrayList<FerryCrossing> ferryCrossings = new ArrayList<>();

    public Ferry(String name){

        ArrayList<AisSignal> aisSignals = Main.storageController.load();
        CrossingController crossingController = new CrossingController();
        ArrayList<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(aisSignals);
        ferryCrossings = crossingController.getFerryCrossings(statusUpdates);
        this.name = name;
    }
    public static Ferry getFerry(){
        return ferry;
    }
    public String getName() {
        return name;
    }

    public ArrayList<FerryCrossing> getFerryCrossings() {
        ArrayList<AisSignal> aisSignals = Main.storageController.load();
        CrossingController crossingController = new CrossingController();
        ArrayList<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(aisSignals);
        ferryCrossings = crossingController.getFerryCrossings(statusUpdates);
        return ferryCrossings;
    }
    
    public void setFerryCrossings(ArrayList<FerryCrossing> ferryCrossings) {
        this.ferryCrossings = ferryCrossings;
    }




}
