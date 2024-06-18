package src;

import org.junit.Test;

import static org.junit.Assert.*;

public class RadioChannelInfoTest {

    String name = "nameTest";
    String type = "typeTest";
    RadioChannelInfo channelInfoTest = new RadioChannelInfo();

    @Test
    public void getNameTest() throws Exception {
        assertNull(channelInfoTest.getName());
    }

    @Test
    public void setNameTest() throws Exception {
        channelInfoTest.setName(name);
        assertTrue(name.equals(channelInfoTest.getName()));
    }

    @Test
    public void getChannelTypeTest() throws Exception {
        assertNull(channelInfoTest.getChannelType());
    }

    @Test
    public void setChannelTypeTest() throws Exception {
        channelInfoTest.setChannelType(type);
        assertTrue(type.equals(channelInfoTest.getChannelType()));
    }



    @Test
    public void getEpisodesInfoListTest() throws Exception {
        assertNotNull(channelInfoTest.getEpisodesInfoList());
    }

    @Test
    public void addEpisodeInfoTest() throws Exception {
        String title = "episodeTest";
        RadioEpisodeInfo episodeInfo = new RadioEpisodeInfo(title);
        channelInfoTest.addEpisodeInfo(episodeInfo);
        assertFalse(channelInfoTest.getEpisodesInfoList().isEmpty());
    }

    @Test
    public void getEpisodeInfoTest() throws Exception {
        String title = "episodeTest";
        RadioEpisodeInfo episodeInfo = new RadioEpisodeInfo(title);
        channelInfoTest.addEpisodeInfo(episodeInfo);
        assertNotNull(channelInfoTest.getEpisodeInfo(title));
    }
}
