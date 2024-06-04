package org.vvamp.ingenscheveer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.json.AisSignal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class LocalFileStorageController implements StorageController{
    private File file;
    public String readData = "";
    private Semaphore mutex = new Semaphore(1);
    public LocalFileStorageController(String filename){
        file = new File(filename);
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

//            file.mkdirs();
        }
        System.out.println(file.getAbsoluteFile());
    }

    @Override
    public void save(AisSignal aisSignal) {
        ArrayList<AisSignal> loadedMessages = new ArrayList<AisSignal>();
        ArrayList<AisSignal> savedMessages = load();

        for (AisSignal message : savedMessages) {
            loadedMessages.add(message);
        }
        loadedMessages.add(aisSignal);

        save(loadedMessages);
    }

    @Override
    public void save(ArrayList<AisSignal> aisSignals) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(aisSignals);
        }catch(Exception e){
            return; // Failed to save
        }
        try {
            mutex.acquire();
            writer.write(json);
            writer.close();
            mutex.release();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }catch(Exception e){
            mutex.release();
        }
    }

    @Override
    public ArrayList<AisSignal> load() {
        String json = "";
        readData = "";
        try {
            mutex.acquire();
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                json += data;
            }
            myReader.close();
            mutex.release();

        } catch (
                FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            return new ArrayList<AisSignal>();
        }catch(Exception e){
            mutex.release();
        }
        readData = json;
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<AisSignal> data = null;
        if(json.trim().isEmpty()){
            return new ArrayList<AisSignal>();
        }
        try {
            data = objectMapper.readValue(json, new TypeReference<ArrayList<AisSignal>>() {});
        }catch(Exception e){
            System.err.println("Failed to read ais data");
            return null;
        }
        return data;
    }
}
