package org.vvamp.ingenscheveer;

import org.vvamp.ingenscheveer.models.MainMessage;

import java.util.ArrayList;

public interface StorageController {
    public void save(MainMessage mainMessage);
    public void save(ArrayList<MainMessage> mainMessages);
    public ArrayList<MainMessage> load();
}
