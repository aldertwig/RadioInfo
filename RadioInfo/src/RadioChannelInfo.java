import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that represents the radio channels. Has a list with episode's
 * information that is played on the radio channel.
 * @author Johan Ahlqvist
 * 2017-12-04
 */
public class RadioChannelInfo {

    private String name = null;         //Name of the channel.
    private String channelType = null;  //Type of channel.
    /* List with episodes played on the channel. */
    private CopyOnWriteArrayList<RadioEpisodeInfo> episodeInfoList;

    /**
     * Constructor for radio channel information.
     */
    public RadioChannelInfo() {
        episodeInfoList = new CopyOnWriteArrayList<RadioEpisodeInfo>();
    }

    /**
     * Gets the name of the channel.
     * @return  String - the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the channel.
     * @param name  String - the name to be set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the channel type.
     * @return  String - the type of channel.
     */
    public String getChannelType() {
        return channelType;
    }

    /**
     * Sets the channel type.
     * @param channelType   String - the type of channel.
     */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /**
     * Adds an episode to the episode list.
     * @param programInfo   RadioEpisodeInfo - the episode.
     */
    public void addEpisodeInfo(RadioEpisodeInfo programInfo) {
        episodeInfoList.add(programInfo);
    }

    /**
     * Gets the list with channel's episodes.
     * @return  CopyOnWriteArrayList - the list with episode information.
     */
    public CopyOnWriteArrayList getEpisodesInfoList() {
        return episodeInfoList;
    }

    /**
     * Gets an episode from episode corresponding to given name.
     * @param episodeName   String - the name of episode.
     * @return  RadioEpisodeInfo - the found episode, else null.
     */
    public RadioEpisodeInfo getEpisodeInfo(String episodeName) {
        for (RadioEpisodeInfo episodeInfo : episodeInfoList) {
            if (episodeInfo.getTitle() == episodeName) {
                return episodeInfo;
            }
        }
        return null;
    }
}
