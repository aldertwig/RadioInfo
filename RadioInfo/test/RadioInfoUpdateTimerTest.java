package src;

import org.junit.Test;

import static org.junit.Assert.*;

public class RadioInfoUpdateTimerTest {
    RadioInfoController controller = new RadioInfoController("xmlAddress");
    RadioInfoUpdateTimer updateTimer;

    @Test
    public void updateTimerTest() {
        assertNull(updateTimer);
    }

    @Test
    public void createUpdateWorkerTest() {
        updateTimer = new RadioInfoUpdateTimer(controller);
        assertNotNull(updateTimer);
    }

}