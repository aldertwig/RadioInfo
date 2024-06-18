package src;

import org.junit.Test;

import static org.junit.Assert.*;

public class RadioInfoUpdateWorkerTest {
    RadioInfoController controller = new RadioInfoController("xmlAddress");
    RadioInfoUpdateWorker updateWorker;

    @Test
    public void updateWorkerTest() {
        assertNull(updateWorker);
    }

    @Test
    public void createUpdateWorkerTest() {
        updateWorker = new RadioInfoUpdateWorker(controller);
        assertNotNull(updateWorker);
    }
}