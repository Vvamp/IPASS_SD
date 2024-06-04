package org.vvamp.ingenscheveer;

import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.util.ArrayList;

public interface StorageController {
    public void save(AisSignal aisSignal);
    public void save(ArrayList<AisSignal> aisSignals);
    public ArrayList<AisSignal> load();
}
