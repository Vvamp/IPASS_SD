package org.vvamp.ingenscheveer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vvamp.ingenscheveer.models.StatusUpdate;
import org.vvamp.ingenscheveer.models.json.MainMessage;
import org.vvamp.ingenscheveer.models.json.Message;
import org.vvamp.ingenscheveer.models.json.PositionReport;

import static org.junit.jupiter.api.Assertions.*;

public class StatusUpdateTest {
    private MainMessage mainMessage;
    @BeforeEach
    public void init(){
        mainMessage = new MainMessage();
        mainMessage.message = new Message();
        mainMessage.message.positionReport = new PositionReport(180f, 0, 52F, 5F, 1, 0, true, true, -128, 0, 0.31F, 0, 0, 13, 511, 244780155, true);

    }

    @Test
    public void testIsSailing_true(){
        mainMessage.message.positionReport.sog = 0.31F;
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, mainMessage);
        assertTrue(statusUpdate.isSailing());
    }

    @Test
    public void testIsSailing_false(){
        mainMessage.message.positionReport.sog = 0.29F;
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, mainMessage);
        assertFalse(statusUpdate.isSailing());
    }

    @Test
    public void testEqual_otherObject(){
        StatusUpdate statusUpdate = new StatusUpdate(Location.UNKNOWN, mainMessage);
        assertNotEquals(statusUpdate, mainMessage, "Status updates are not equal to any other object");
    }

    @Test
    public void testEqual_otherObjectSameLocationAndStatus(){
        StatusUpdate statusUpdate = new StatusUpdate(Location.INGEN, mainMessage);
        StatusUpdate secondStatusUpdate = new StatusUpdate(Location.INGEN, mainMessage);
        assertEquals(statusUpdate, secondStatusUpdate, "Two status updates are equal if the location and isSailing variables are equal");
    }

    @Test
    public void testEqual_SameInstance(){
        StatusUpdate statusUpdate = new StatusUpdate(Location.INGEN, mainMessage);
        StatusUpdate secondStatusUpdate = statusUpdate;
        assertEquals(statusUpdate, secondStatusUpdate, "Two status updates are always equal if they are the same object");
    }
}
