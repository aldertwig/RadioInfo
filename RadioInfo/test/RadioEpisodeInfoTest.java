package src;

import org.junit.Test;

import static org.junit.Assert.*;

public class RadioEpisodeInfoTest {

    String title = "episodeTest";
    RadioEpisodeInfo episodeInfo = new RadioEpisodeInfo(title);

    @Test
    public void createRadioEpisodeInfoTest() {
        RadioEpisodeInfo episodeInfoTest = new RadioEpisodeInfo(title);
        assertNotNull(episodeInfoTest);
    }

    @Test
    public void getTitle() throws Exception {
        assertTrue(title.equals(episodeInfo.getTitle()));
    }

    @Test
    public void getStartTimeUTC() throws Exception {
        assertNull(episodeInfo.getStartTimeUTC());
    }

    @Test
    public void setStartTimeUTC() throws Exception {
        String time = "timeTest";
        episodeInfo.setStartTimeUTC(time);
        assertTrue(time.equals(episodeInfo.getStartTimeUTC()));
    }

    @Test
    public void getEndTimeUTC() throws Exception {
        assertNull(episodeInfo.getEndTimeUTC());
    }

    @Test
    public void setEndTimeUTC() throws Exception {
        String time = "timeTest";
        episodeInfo.setEndTimeUTC(time);
        assertTrue(time.equals(episodeInfo.getEndTimeUTC()));
    }

    @Test
    public void getImageURL() throws Exception {
        assertNull(episodeInfo.getImageURL());
    }

    @Test
    public void setImageURL() throws Exception {
        String image = "imageTest";
        episodeInfo.setImageURL(image);
        assertTrue(image.equals(episodeInfo.getImageURL()));
    }

    @Test
    public void getDescription() throws Exception {
        assertNull(episodeInfo.getDescription());
    }

    @Test
    public void setDescription() throws Exception {
        String description = "descriptionTest";
        episodeInfo.setDescription(description);
        assertTrue(description.equals(episodeInfo.getDescription()));
    }

}