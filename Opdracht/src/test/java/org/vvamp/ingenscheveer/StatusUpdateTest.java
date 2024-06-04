package org.vvamp.ingenscheveer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.StatusUpdate;
import org.vvamp.ingenscheveer.models.json.AisSignal;
import org.vvamp.ingenscheveer.models.json.Message;
import org.vvamp.ingenscheveer.models.json.MetaData;
import org.vvamp.ingenscheveer.models.json.PositionReport;
import org.vvamp.ingenscheveer.webservices.AISResource;
import org.vvamp.ingenscheveer.webservices.FerryCrossingResource;
import java.net.URL;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StatusUpdateTest {
    private AisSignal aisSignal;
    @BeforeEach
    public void init(){
        aisSignal = new AisSignal();
        aisSignal.message = new Message();
        aisSignal.message.positionReport = new PositionReport(180f, 0, 52F, 5F, 1, 0, true, true, -128, 0, 0.31F, 0, 0, 13, 511, 244780155, true);

    }

    @Test
    public void testIsSailing_true(){
        aisSignal.message.positionReport.sog = 0.31F;
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, aisSignal);
        assertTrue(statusUpdate.isSailing());
    }

    @Test
    public void testIsSailing_false(){
        aisSignal.message.positionReport.sog = 0.29F;
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, aisSignal);
        assertFalse(statusUpdate.isSailing());
    }

    @Test
    public void testEqual_otherObject(){
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, aisSignal);
        assertNotEquals(statusUpdate, aisSignal, "Status updates are not equal to any other object");
    }

    @Test
    public void testEqual_otherObjectSameLocationAndStatus(){
        StatusUpdate statusUpdate = new StatusUpdate(Location.INGEN, aisSignal);
        StatusUpdate secondStatusUpdate = new StatusUpdate(Location.INGEN, aisSignal);
        assertEquals(statusUpdate, secondStatusUpdate, "Two status updates are equal if the location and isSailing variables are equal");
    }

    @Test
    public void testEqual_SameInstance(){
        StatusUpdate statusUpdate = new StatusUpdate(Location.INGEN, aisSignal);
        StatusUpdate secondStatusUpdate = statusUpdate;
        assertEquals(statusUpdate, secondStatusUpdate, "Two status updates are always equal if they are the same object");
    }

    // Integratie Tests
    @Test
    public void integrationTest_getCrossings(){
        Ferry ferry = Ferry.getFerry();

        ClassLoader classLoader = StatusUpdateTest.class.getClassLoader();
        URL resource = classLoader.getResource("integrationtest.json");

        if(resource == null){
            fail("The integrationtest.json file does not exist");
        }

        File integrationTestFile = new File(resource.getFile());

        LocalFileStorageController lfc = new LocalFileStorageController(integrationTestFile.getAbsolutePath());
        ArrayList<AisSignal> signals = lfc.load();
        CrossingController crossingController = new CrossingController();
        ArrayList<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(signals);
        ArrayList<FerryCrossing> ferryCrossings = crossingController.getFerryCrossings(statusUpdates);
        ferry.setFerryCrossings(ferryCrossings);

        ObjectMapper mapper = new ObjectMapper();
        String json_before = null;
        try {
            json_before = mapper.writeValueAsString(ferryCrossings);
        } catch (JsonProcessingException e) {
            fail("The crossings api should give back a valid json string");
        }

        Main.storageController = lfc;

        FerryCrossingResource ferryCrossingResource = new FerryCrossingResource();
        String json_after = (String)ferryCrossingResource.getCrossings().getEntity();

        assertFalse(json_before.isEmpty(), "The returned json shouldn't be empty");
        assertEquals(json_before, json_after, "The crossings API should work correctly.");
    }
}
