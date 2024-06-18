package src;

import org.junit.Test;
import javax.swing.*;
import static org.junit.Assert.*;

public class RadioInfoFrameTest {
    RadioInfoController controller = new RadioInfoController("xmlAddress");
    RadioInfoFrame radioInfoFrame = new RadioInfoFrame("Test", controller);

    @Test
    public void testFrame() throws Exception {
        assertNotNull(radioInfoFrame);
    }

    @Test
    public void testIsVisible() throws Exception {
        assertFalse(radioInfoFrame.isVisible());
    }

    @Test
    public void testMenuBarItems() throws Exception {
        JMenuBar menuBar = radioInfoFrame.createMenus(controller);
        assertNotNull(menuBar);
        assertEquals(2, menuBar.getComponentCount());
    }

    @Test
    public void testMenuExitItem() throws Exception {
        JMenuBar menuBar = radioInfoFrame.createMenus(controller);

        JMenu menu = (JMenu) menuBar.getComponent(0);
        assertNotNull(menu);
        assertEquals(1, menu.getItemCount());

        JMenuItem exitItem = menu.getItem(0);
        assertEquals("Exit", exitItem.getText());
        assertNotNull(exitItem.getItemListeners());
        assertTrue(exitItem.isVisible());
        assertTrue(exitItem.isEnabled());
    }

    @Test
    public void testMenuUpdateItem() throws Exception {
        JMenuBar menuBar = radioInfoFrame.createMenus(controller);

        JMenu menu = (JMenu) menuBar.getComponent(1);
        assertNotNull(menu);
        assertEquals(1,menu.getItemCount());

        JMenuItem updateItem = menu.getItem(0);
        assertEquals("Update", updateItem.getText());
        assertNotNull(updateItem.getItemListeners());
        assertTrue(updateItem.isVisible());
        assertTrue(updateItem.isEnabled());
    }

    @Test
    public void testChannelsTable() throws Exception {
        JTable table = radioInfoFrame.buildChannelsTable(controller);
        assertNotNull(table);
        assertEquals(1, table.getComponentCount());
    }

    @Test
    public void testEpisodesTable() throws Exception {
        JTable table = radioInfoFrame.buildEpisodesTable(controller);
        assertNotNull(table);
        assertEquals(1, table.getComponentCount());
    }

    @Test
    public void testChannelsPanel() throws Exception {
        JPanel panel = radioInfoFrame.buildChannelsPanel(controller);
        assertNotNull(panel);
        assertEquals(1, panel.getComponentCount());
    }

    @Test
    public void testEpisodesPanel() throws Exception {
        JPanel panel = radioInfoFrame.buildEpisodesPanel(controller);
        assertNotNull(panel);
        assertEquals(1, panel.getComponentCount());
    }

    @Test
    public void testUpdateFrame() throws Exception {
        assertNull(radioInfoFrame.getUpdateFrame());
    }

    @Test
    public void testCreateUpdateFrame() {
        radioInfoFrame.createUpdateFrame(100);
        assertNotNull(radioInfoFrame.getUpdateFrame());
        assertFalse(radioInfoFrame.getUpdateFrame().isClosable());
        assertFalse(radioInfoFrame.getUpdateFrame().isResizable());
        assertTrue(radioInfoFrame.getUpdateFrame().isVisible());
    }

    @Test
    public void testUpdateFrameShowing() {
        radioInfoFrame.createUpdateFrame(100);
        assertTrue(radioInfoFrame.getUpdateFrame().isVisible());
    }

    @Test
    public void testUpdateFrameClosable() {
        radioInfoFrame.createUpdateFrame(100);
        assertFalse(radioInfoFrame.getUpdateFrame().isClosable());
    }

    @Test
    public void testUpdateFrameResizable() {
        radioInfoFrame.createUpdateFrame(100);
        assertFalse(radioInfoFrame.getUpdateFrame().isResizable());
    }

    @Test
    public void testDisposeUpdateFrame() {
        radioInfoFrame.createUpdateFrame(100);
        radioInfoFrame.disposeUpdateFrame();
        assertFalse(radioInfoFrame.getUpdateFrame().isVisible());
    }
}