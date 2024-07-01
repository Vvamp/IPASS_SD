package org.vvamp.ingenscheveer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vvamp.ingenscheveer.database.DatabaseStorageController;
import org.vvamp.ingenscheveer.database.models.AisData;
import org.vvamp.ingenscheveer.models.Ferry;
import org.vvamp.ingenscheveer.models.FerryCrossing;
import org.vvamp.ingenscheveer.models.StatusUpdate;
import org.vvamp.ingenscheveer.models.json.AisSignal;
import org.vvamp.ingenscheveer.models.json.Message;
import org.vvamp.ingenscheveer.models.json.MetaData;
import org.vvamp.ingenscheveer.models.json.PositionReport;
import org.vvamp.ingenscheveer.webservices.FerryCrossingResource;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StatusUpdateTest {
    private AisData aisData;

    @BeforeEach
    public void init() {
        AisSignal aisSignal = new AisSignal();
        aisSignal.setMessage(new Message());
        aisSignal.getMessage().setPositionReport(new PositionReport(180f, 0, 52F, 5F, 1, 0, true, true, -128, 0, 0.31F, 0, 0, 13, 511, 244780155, true));
        aisSignal.setMetaData(new MetaData());
        aisSignal.getMetaData().setTime_utc("2024-06-30 12:12:11.207450155 +0000 UTC");
        aisData = DatabaseStorageController.getDatabaseAisController().convertToAisData(aisSignal);
    }

    @Test
    public void testIsSailing_true() {
        aisData.setSog(0.21F);
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, aisData);
        assertTrue(statusUpdate.isSailing());
    }

    @Test
    public void testIsSailing_false() {
        aisData.setSog(0.19F);
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, aisData);
        assertFalse(statusUpdate.isSailing());
    }

    @Test
    public void testEqual_otherObject() {
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, aisData);
        assertNotEquals(statusUpdate, aisData, "Status updates are not equal to any other object");
    }

    @Test
    public void testEqual_otherObjectSameLocationAndStatus() {
        StatusUpdate statusUpdate = new StatusUpdate(Location.INGEN, aisData);
        StatusUpdate secondStatusUpdate = new StatusUpdate(Location.INGEN, aisData);
        assertEquals(statusUpdate, secondStatusUpdate, "Two status updates are equal if the location and isSailing variables are equal");
    }

    @Test
    public void testEqual_SameInstance() {
        StatusUpdate statusUpdate = new StatusUpdate(Location.INGEN, aisData);
        StatusUpdate secondStatusUpdate = statusUpdate;
        assertEquals(statusUpdate, secondStatusUpdate, "Two status updates are always equal if they are the same object");
    }

    // Integratie Tests
    @Test
    public void integrationTest_getCrossings() {
        Ferry ferry = Ferry.getFerry();

        ClassLoader classLoader = StatusUpdateTest.class.getClassLoader();
        URL resource = classLoader.getResource("integrationtest.json");

        if (resource == null) {
            fail("The integrationtest.json file does not exist");
        }

        File integrationTestFile = new File(resource.getFile());

        LocalFileStorageController lfc = new LocalFileStorageController(integrationTestFile.getAbsolutePath());
        List<AisSignal> signals = lfc.load();
        List<AisData> data = new ArrayList<>();
        for (AisSignal signal : signals) {
            data.add(DatabaseStorageController.getDatabaseAisController().convertToAisData(signal));
        }
        DatabaseStorageController.setUseTest(true);
        DatabaseStorageController.getDatabaseAisController().removeAll();
        for (AisData dataItem : data) {
            DatabaseStorageController.getDatabaseAisController().writeAisData(dataItem);
        }

        CrossingController crossingController = new CrossingController();
        List<StatusUpdate> statusUpdates = crossingController.getStatusUpdates(data);
        List<FerryCrossing> ferryCrossings = crossingController.getFerryCrossings(statusUpdates);
        ferry.setFerryCrossings(ferryCrossings);

        Collections.reverse(ferryCrossings);
        ObjectMapper mapper = JsonMapper.builder().configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false).build();
        String json_before = null;
        try {
            json_before = mapper.writeValueAsString(ferryCrossings);
        } catch (JsonProcessingException e) {
            fail("The crossings api should give back a valid json string");
        }

        FerryCrossingResource ferryCrossingResource = new FerryCrossingResource();
        String json_after = null;
        List<FerryCrossing> newCrossings = new ArrayList<>();
        try {
            json_after = mapper.writeValueAsString(ferryCrossingResource.getCrossings(-1).getEntity());
        } catch (JsonProcessingException e) {
            fail("The crossings api should give back a valid json string");
        }

        assertFalse(json_before.isEmpty(), "The returned json shouldn't be empty");
        assertEquals(json_before, json_after, "The crossings API should work correctly.");
    }
}
