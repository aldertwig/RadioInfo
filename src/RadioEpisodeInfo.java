/**
 * Class that represents the channel episodes.
 * @author Johan Ahlqvist
 * 2017-12-04
 */
public class RadioEpisodeInfo {

    private String title = null;        //The episode's title.
    private String description = null;  //The episode's description.
    private String startTimeUTC = null; //The episode's start time.
    private String endTimeUTC = null;   //The episode's end time.
    private String imageURL = null;     //The episode's image url.

    /**
     * Constructor for RadioEpisodeInfo. Used for holding information about a
     * episode.
     * @param title String - the episode's title.
     */
    public RadioEpisodeInfo(String title) {
        setTitle(title);
    }

    /**
     * Gets the episode's title.
     * @return  String - the title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the episode's title.
     * @param title String - the title.
     */
    private void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the starting time.
     * @return  String - start time.
     */
    public String getStartTimeUTC() {
        return startTimeUTC;
    }

    /**
     * Sets the starting time.
     * @param startTimeUTC  String - the start time.
     */
    public void setStartTimeUTC(String startTimeUTC) {
        this.startTimeUTC = startTimeUTC;
    }

    /**
     * Gets the ending time.
     * @return  String - end time.
     */
    public String getEndTimeUTC() {
        return endTimeUTC;
    }

    /**
     * Sets the ending time.
     * @param endTimeUTC    String - the end time.
     */
    public void setEndTimeUTC(String endTimeUTC) {
        this.endTimeUTC = endTimeUTC;
    }

    /**
     * Gets the image url.
     * @return  String - image url.
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * Sets the image url.
     * @param imageURL  String - the image url.
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Gets the episode's description.
     * @return  String - description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the episode's description.
     * @param description   String - the description.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
