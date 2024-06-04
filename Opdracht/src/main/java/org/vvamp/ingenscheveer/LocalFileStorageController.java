package org.vvamp.ingenscheveer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.vvamp.ingenscheveer.models.json.MainMessage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class LocalFileStorageController implements StorageController{
    private File file;
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
    public void save(MainMessage mainMessage) {
        ArrayList<MainMessage> loadedMessages = new ArrayList<MainMessage>();
        ArrayList<MainMessage> savedMessages = load();

        for (MainMessage message : savedMessages) {
            loadedMessages.add(message);
        }
        loadedMessages.add(mainMessage);

        save(loadedMessages);
    }

    @Override
    public void save(ArrayList<MainMessage> mainMessages) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file.getAbsoluteFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(mainMessages);
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
    public ArrayList<MainMessage> load() {
        String json = "";
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
            return new ArrayList<MainMessage>();
        }catch(Exception e){
            mutex.release();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<MainMessage> data = null;
        if(json.trim().isEmpty()){
            return new ArrayList<MainMessage>();
        }
        try {
            data = objectMapper.readValue(json, new TypeReference<ArrayList<MainMessage>>() {});
        }catch(Exception e){
            return null;
        }
        return data;
    }
}
