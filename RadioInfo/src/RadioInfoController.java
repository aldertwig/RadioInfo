import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Class that represents the controller.
 * @author Johan Ahlqvist
 * 2017-12-28
 */
public class RadioInfoController {

    private static RadioInfoController controller;      //The controller (this).
    private RadioInfoUpdateTimer infoUpdater = null;    //The update timer.
    private Timer timer = null;         //The timer.
    private int timeInterval = 3600;    //Interval in seconds for timer.
    /* A list with radio channel information. */
    private static CopyOnWriteArrayList<RadioChannelInfo> channelInfoList;

    private RadioInfoXMLParser radioInfoXMLParser = null;   //XML parser.
    private RadioChannelInfo selectedChannel = null;        //Selected channel.
    private RadioInfoFrame radioInfoGui = null;             //The gui.

    private final String xmlAddress;    //The xml address.
    private static Date updateTimeDate; //Time date for update.
    private static Date afterTimeDate;  //Time date after update.
    private static Date beforeTimeDate; //Time date before update.
    private final int hoursBefore = 12; //Hours before update time.
    private final int hoursAfter = 12;  //Hours after update time.
    /* An update worker. */
    private static SwingWorker<Boolean, Integer> updateWorker;
    /* Boolean for update is in progress. */
    private static boolean isUpdating = false;
    /* The date format. */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat
                                                ("yyyy-MM-dd'T'HH:mm:ss'Z'");
    /* The time format. */
    private final SimpleDateFormat timeFormat = new SimpleDateFormat
                                                ("HH:mm:ss");

    /**
     * Creates a controller for RadioInfo.
     * @param xmlAddress    String - the address to the xml.
     */
    public RadioInfoController(String xmlAddress) {
        controller = this;
        this.xmlAddress = xmlAddress;
        createXMLParser();
    }

    /**
     * Gets the date format.
     * @return  SimpleDateFormat - format for date.
     */
    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    /**
     * Gets the time format.
     * @return  SimpleDateFormat - format for time.
     */
    public SimpleDateFormat getTimeFormat() {
        return timeFormat;
    }

    /**
     * Sets the channel that has been selected.
     * @param selectedChannel   RadioChannelInfo - the selected channel.
     */
    public void setSelectedChannel(RadioChannelInfo selectedChannel) {
        this.selectedChannel = selectedChannel;
    }

    /**
     * Creates a new xml parser.
     */
    private void createXMLParser() {
        radioInfoXMLParser = new RadioInfoXMLParser(xmlAddress);
    }

    /**
     * Updates all the channel and episode information.
     */
    public synchronized void runInfoUpdater() {
        setTimeDates();
        createChannelInfoList();
        createUpdateWorker();
        runUpdateWorker();
    }

    /**
     * Creates a new update worker.
     */
    private void createUpdateWorker() {
        updateWorker = new RadioInfoUpdateWorker(controller);
    }

    /**
     * Runs the update worker.
     */
    private void runUpdateWorker() {
        updateWorker.execute();
    }

    /**
     * Stops the update worker.
     */
    private void stopUpdateWorker() {
        updateWorker.cancel(true);
    }

    private void createChannelInfoList() {
        channelInfoList = new CopyOnWriteArrayList<RadioChannelInfo>();
    }

    /**
     * Gets the time date and time date for hours before/after.
     */
    private void setTimeDates() {
        Calendar updateTimeCalendar = Calendar.getInstance();
        updateTimeDate = updateTimeCalendar.getTime();

        updateTimeCalendar.add(Calendar.HOUR_OF_DAY, -hoursBefore);
        beforeTimeDate = updateTimeCalendar.getTime();

        updateTimeCalendar.add(Calendar.HOUR_OF_DAY, hoursAfter + hoursBefore);
        afterTimeDate = updateTimeCalendar.getTime();
    }

    /**
     * Checks if there is an update running.
     * @return  boolean - true if update is running, else false;
     */
    public synchronized boolean isUpdating() {
        return isUpdating;
    }

    /**
     * Sets if there is an update running.
     * @param updating  boolean - true if update is running, else false;
     */
    public synchronized void setIsUpdating(boolean updating) {
        isUpdating = updating;
        /* If a gui has been set. */
        if (guiIsSet()) {
            if (updating) {
                /* Unable updating. */
                radioInfoGui.enableUpdate(false);
            } else {
                /* Enable updating. */
                radioInfoGui.enableUpdate(true);
            }
        }
    }

    /**
     * Checks if a gui has been set (not null).
     * @return  boolean - false if gui is null, else true.
     */
    public boolean guiIsSet() {
        return (radioInfoGui != null);
    }

    /**
     * Creates a frame in view to display update progress.
     * @param totalValue    int - the total amount of data to read.
     */
    public synchronized void createProgressUpdate(int totalValue) {
        radioInfoGui.createUpdateFrame(totalValue);
    }

    /**
     * Updates the progress display in the view.
     * @param value int - the amount of data that has been read.
     */
    public synchronized void showProgressUpdate(int value) {
        radioInfoGui.updateProgress(value);
    }

    /**
     * Gets a RadioChannelInfo model from channel-info list.
     * @param channelName   String - the channel name.
     * @return  RadioChannelInfo - the found channel, else null.
     */
    public RadioChannelInfo getChannelInfo(String channelName) {
        for (RadioChannelInfo channelInfo : channelInfoList) {
            if (channelInfo.getName() == channelName) {
                return channelInfo;
            }
        }
        return null;
    }

    /**
     * Gets the list of RadioChannelInfo models.
     * @return  CopyOnWriteArrayList - the list.
     */
    public CopyOnWriteArrayList<RadioChannelInfo> getChannelInfoList() {
        return channelInfoList;
    }

    /**
     * Gets episode-info by given name from the selected channel.
     * @param episodeName   String - the episode name.
     * @return  RadioEpisodeInfo - the episode's information.
     */
    public RadioEpisodeInfo getEpisodeInfo(String episodeName) {
        return selectedChannel.getEpisodeInfo(episodeName);
    }

    /**
     * Creates a new timer for updating.
     */
    public void createInfoUpdater() {
        timer = new Timer();
        setTimeInterval();
        infoUpdater = new RadioInfoUpdateTimer(controller);
        timer.scheduleAtFixedRate(infoUpdater, 0, timeInterval);
    }

    /**
     * Sets the time interval from milliseconds to seconds.
     */
    public void setTimeInterval() {
        timeInterval = timeInterval * 1000;
    }

    /**
     * Sets the gui.
     * @param frame RadioInfoFrame - the frame to be set.
     */
    public void setGui(RadioInfoFrame frame) {
        radioInfoGui = frame;
    }

    /**
     * Gets the channel that the user has selected.
     * @return  RadioChannelInfo - the radio channel's information.
     */
    public RadioChannelInfo getSelectedChannel() {
        return selectedChannel;
    }

    /**
     * Checks if a given episode has ended by checking last time updated.
     * @param episodeName   String - the episode's title.
     * @return  boolean - true if the episode has ended, else false.
     */
    public boolean hasEpisodeEnded(String episodeName) {
        try {
            RadioEpisodeInfo episodeInfo = getEpisodeInfo(episodeName);
            Date endTime = dateFormat.parse(episodeInfo.getEndTimeUTC());
            return updateTimeDate.after(endTime);
        } catch (ParseException e) {
            guiShowMessage(e.toString());
            return false;
        }
    }

    /**
     * Checks if a channel has been selected.
     * @return  boolean - true if a channel has been selected, else false.
     */
    public boolean hasSelectedChannel() {
        return selectedChannel != null;
    }

    /**
     * Sends a message to the gui to be shown in a message frame.
     * @param message   String - the message to be shown.
     */
    public void guiShowMessage(String message) {
        radioInfoGui.showMessage(message);
    }

    /**
     * Tells the gui to show the results from the last update.
     */
    public void updateDone() {
        presentResults();
    }

    /**
     * Present results by showing information in channel and episode tables.
     */
    public void presentResults() {
        radioInfoGui.resetChannelTable();
        /* For each channel, add the channel's information to channel table. */
        for (RadioChannelInfo channelInfo : channelInfoList) {
            radioInfoGui.addChannelToTable(channelInfo.getName(),
                                           channelInfo.getChannelType());
            /* If the channel is selected, add channel's episodes to the
             * episode table.
             */
            if (selectedChannel != null &&
                    (selectedChannel.getName() == channelInfo.getName())) {
                CopyOnWriteArrayList episodesInfoList;
                episodesInfoList = channelInfo.getEpisodesInfoList();
                /* For each episode, add the episode's information to the
                 * episode table.
                 */
                for (int i = 0; i < episodesInfoList.size(); i++) {
                    RadioEpisodeInfo episodeInfo = (RadioEpisodeInfo)
                                                   episodesInfoList.get(i);

                    radioInfoGui.addEpisodeToTable(episodeInfo.getTitle(),
                                                   getStartTime(episodeInfo),
                                                   getEndTime(episodeInfo));
                }
            }
        }
    }

    /**
     * Gets the xml parser.
     * @return  RadioInfoXMLParser - the xml parser.
     */
    public RadioInfoXMLParser getXMLParser() {
        return radioInfoXMLParser;
    }

    /**
     * Checks if a start and end time is inside the before and after time span.
     * @param startTimeUTC  String - the start time.
     * @param endTimeUTC    String - the end time.
     * @return  boolean - true if the times are inside before and after time
     *                    span,else false.
     */
    public boolean checkTimeDate(String startTimeUTC, String endTimeUTC) {
        try {
            Date start = dateFormat.parse(startTimeUTC);
            Date end = dateFormat.parse(endTimeUTC);
            boolean isAfter = end.after(beforeTimeDate);
            boolean isBefore = start.before(afterTimeDate);
            return (isAfter && isBefore);
        } catch (ParseException e) {
            guiShowMessage(e.toString());
            return false;
        }
    }

    /**
     * Gets the start date time format from episode.
     * @param episodeInfo RadioEpisodeInfo - the episode.
     * @return  String - the start time.
     */
    public String getStartTime(RadioEpisodeInfo episodeInfo) {
        try {
            return timeFormat.format(dateFormat.parse(episodeInfo.
                                                      getStartTimeUTC()));
        } catch (ParseException e) {
            radioInfoGui.showMessage(e.toString());
            return "";
        }
    }

    /**
     * Gets the end date time format from episode.
     * @param episodeInfo RadioEpisodeInfo - the episode.
     * @return  String - the start time.
     */
    private String getEndTime(RadioEpisodeInfo episodeInfo) {
        try {
            return timeFormat.format(dateFormat.parse(episodeInfo.
                                                      getEndTimeUTC()));
        } catch (ParseException e) {
            radioInfoGui.showMessage(e.toString());
            return "";
        }
    }

    /**
     * Action listener for the quit menu item.
     */
    class QuitItemListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (infoUpdater != null) {
                infoUpdater.cancel();
            }
            if (timer != null) {
                timer.cancel();
            }
            radioInfoGui.quitOption();
        }
    }

    /**
     * Action listener for the update menu item.
     */
    class UpdateItemListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            runInfoUpdater();
        }
    }

    /**
     * Action listener for the channel list.
     */
    class ChannelListListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent le) {
            if (radioInfoGui.getChannelRowCount() > 0) {
                    /* If channel selected. */
                if (le.getValueIsAdjusting() == false) {
                    radioInfoGui.resetEpisodeTable();
                    int row = radioInfoGui.getSelectedChannelRow();
                    int col = 0;
                    String selectedName = radioInfoGui.getChannelName(row, col);
                    /* Set selected channel. */
                    setSelectedChannel(getChannelInfo(selectedName));
                    if (hasSelectedChannel()) {
                        CopyOnWriteArrayList episodesInfoList;
                        episodesInfoList = getSelectedChannel().
                                getEpisodesInfoList();
                        /* Add channel episodes to episode table. */
                        for (int i = 0; i < episodesInfoList.size(); i++) {
                            RadioEpisodeInfo episodeInfo;
                            episodeInfo = (RadioEpisodeInfo)
                                          episodesInfoList.get(i);
                            String title = episodeInfo.getTitle();
                            String start = getStartTime(episodeInfo);
                            String end = getEndTime(episodeInfo);
                            radioInfoGui.addEpisodeToTable(title, start, end);
                        }
                    }
                }
            }
        }
    }

    /**
     * Action listener for the episode list.
     */
    class EpisodeListListener implements ListSelectionListener {

        @Override
        public void valueChanged(ListSelectionEvent le) {

            if (radioInfoGui.getEpisodeRowCount() > 0) {
                /* If episode selected. */
                if (le.getValueIsAdjusting() == true) {
                    int row = radioInfoGui.getSelectedEpisodeRow();
                    int col = 0;
                    String episodeName = radioInfoGui.getEpisodeName(row, col);

                    RadioEpisodeInfo episodeInfo;
                    episodeInfo = getEpisodeInfo(episodeName);

                    String title = episodeInfo.getTitle();
                    String description = episodeInfo.getDescription();
                    String time = getStartTime(episodeInfo) + " - " +
                                  getEndTime(episodeInfo);
                    String imageURLstring = episodeInfo.getImageURL();

                    /* Create and show episode frame. */
                    radioInfoGui.createEpisodeFrame(title, description,
                                                    imageURLstring, time);
                }
            }
        }
    }
}
