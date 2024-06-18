package src;

import org.junit.Test;

import javax.swing.*;

import static org.junit.Assert.*;

public class RadioInfoControllerTest {
    String address = "xmlAddress";
    RadioInfoController controller = new RadioInfoController(address);

    @Test
    public void getDateFormatTest() throws Exception {
        assertNotNull(controller.getDateFormat());
    }

    @Test
    public void getTimeFormatTest() throws Exception {
        assertNotNull(controller.getTimeFormat());
    }

    @Test
    public void isUpdatingTest() throws Exception {
        assertFalse(controller.isUpdating());
    }

    @Test
    public void setIsUpdatingTest() throws Exception {
        controller.setIsUpdating(true);
        assertTrue(controller.isUpdating());
    }

    @Test
    public void guiIsSetTest() throws Exception {
        assertFalse(controller.guiIsSet());
    }

    @Test
    public void setGuiTest() throws Exception {
        RadioInfoFrame gui = new RadioInfoFrame("test", controller);
        controller.setGui(gui);
        assertTrue(controller.guiIsSet());
    }

    @Test
    public void hasSelectedChannelTest() throws Exception {
        assertFalse(controller.hasSelectedChannel());
    }

    @Test
    public void setSelectedChannelTest() throws Exception {
        RadioChannelInfo channelInfo = new RadioChannelInfo();
        controller.setSelectedChannel(channelInfo);
        assertTrue(controller.hasSelectedChannel());
    }

    @Test
    public void getXMLParserTest() throws Exception {
        assertNotNull(controller.getXMLParser());
    }

}