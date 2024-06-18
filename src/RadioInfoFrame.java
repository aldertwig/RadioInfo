import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Class that represents the view. Uses tables and desktop frames to show
 * information. Uses menus to update and quit.
 * @author Johan Ahlqvist
 * 2017-12-28
 */
public class RadioInfoFrame extends JFrame {

	private JFrame radioInfoFrame;              //The view.
	private DefaultTableModel channelTable;     //Table for channels.
    private JTable channelJTable;               //JTable for channels table.
    private DefaultTableModel episodeTable;     //Table for episodes.
    private JTable episodeJTable;               //JTable for episodes table.
    private JDesktopPane desktopPane;           //Desktop for internal frames.
    private JProgressBar updateProgress;        //Progress bar.
    private JInternalFrame updateFrame;         //Update frame showing progress.
    private JMenuItem updateItem;               //Menu item for updating.
    private JMenuItem quitItem;                 //Menu item for quiting.

    /**
     * Constructor for the frame. Takes frame name and the controller as
     * parameters.
     * @param name          String - title for frame.
     * @param controller    RadioInfoController - the controller.
     */
	public RadioInfoFrame(String name, RadioInfoController controller) {
	    radioInfoFrame = new JFrame(name);
        radioInfoFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        radioInfoFrame.setMinimumSize(new Dimension(600,600));
        radioInfoFrame.setLayout(new BorderLayout());
        /* Build menus. */
        radioInfoFrame.setJMenuBar(createMenus(controller));
        /* Build panels. */
        JPanel channelsPanel = buildChannelsPanel(controller);
        JPanel episodesPanel = buildEpisodesPanel(controller);
        JPanel desktopPanel = buildDesktopPanel();
        /* Add panels to the frame. */
        radioInfoFrame.add(channelsPanel, BorderLayout.WEST);
        radioInfoFrame.add(episodesPanel, BorderLayout.CENTER);
        radioInfoFrame.add(desktopPanel, BorderLayout.SOUTH);
        radioInfoFrame.pack();
    }

    /**
     * Creates a menu bar with menus and menu items.
     * @param c    RadioInfoController - the controller.
     * @return  JMenuBar - the menu bar.
     */
    public JMenuBar createMenus(RadioInfoController c) {
	    JMenuBar menuBar = new JMenuBar();
	    JMenu programMenu = new JMenu("Program");
        JMenu optionsMenu = new JMenu("Options");
	    updateItem = new JMenuItem("Update");
        RadioInfoController.UpdateItemListener updateListener = c.new
                                                        UpdateItemListener();
        updateItem.addActionListener(updateListener);
        enableUpdate(true);

	    quitItem = new JMenuItem("Exit");
	    RadioInfoController.QuitItemListener quitListener = c.new
                                                            QuitItemListener();
	    quitItem.addActionListener(quitListener);

        programMenu.add(quitItem);
	    optionsMenu.add(updateItem);
        menuBar.add(programMenu);
        menuBar.add(optionsMenu);

        return menuBar;
    }

    /**
     * Builds the panel for viewing channels.
     * @param controller RadioInfoController - the controller.
     * @return  JPanel - the panel.
     */
    public JPanel buildChannelsPanel(final RadioInfoController controller) {
	    JPanel panel = new JPanel();
	    panel.setBorder(BorderFactory.createTitledBorder("Channel list"));
	    panel.setLayout(new BorderLayout());
	    /* Build the table. */
        JTable table = buildChannelsTable(controller);

        int HEIGHT = 100;
        int WIDTH = 300;
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scrollPane.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        panel.add(scrollPane);

	    return panel;
    }

    /**
     * Builds the table that show channels.
     * @param c RadioInfoController - the controller.
     * @return  JTable - the table.
     */
    public JTable buildChannelsTable(final RadioInfoController c) {
	    channelTable = new DefaultTableModel();
	    channelTable.addColumn("Name");
	    channelTable.addColumn("Type");

        channelJTable = new JTable(channelTable) {
            @Override
            public boolean isCellEditable ( int row, int column )
            {
                return false;
            }
        };
        channelJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /* Add a list selection listener. */
        channelJTable.getSelectionModel().addListSelectionListener(c.
                                          new ChannelListListener());

        return channelJTable;
    }

    /**
     * Builds the panel for viewing episodes.
     * @param c RadioInfoController - the controller.
     * @return  JPanel - the panel.
     */
    public JPanel buildEpisodesPanel(final RadioInfoController c) {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Program list"));
        panel.setLayout(new BorderLayout());
        /* Build the table. */
        JTable table = buildEpisodesTable(c);

        int HEIGHT = 100;
        int WIDTH = 300;
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        scrollPane.setMinimumSize(new Dimension(WIDTH, HEIGHT));
        panel.add(scrollPane);
        return panel;
    }

    /**
     * Builds the table that show episodes.
     * @param c RadioInfoController - the controller.
     * @return  JTable - the table.
     */
    public JTable buildEpisodesTable(final RadioInfoController c) {
        episodeTable = new DefaultTableModel();
        episodeTable.addColumn("Name");
        episodeTable.addColumn("Time");

        episodeJTable = new JTable(episodeTable) {
            @Override
            public boolean isCellEditable ( int row, int column ) {
                return false;
            }

            /**
             * Renderer for graying out ended episodes.
             * @param r         TableCellRenderer - the renderer.
             * @param row       int - the row.
             * @param column    int - the column.
             * @return  Component - the colored component.
             */
            public Component prepareRenderer(TableCellRenderer r, int row,
                                             int column) {
                Component comp = super.prepareRenderer(r, row, column);
                if (c.hasEpisodeEnded(episodeTable.getValueAt(row, 0).
                                      toString())) {
                    comp.setForeground(Color.gray);
                } else {
                    comp.setForeground(Color.black);
                }

                return comp;
            }
        };

        /* Add a list selection listener. */
        episodeJTable.getSelectionModel().addListSelectionListener(c.new
                                                        EpisodeListListener());

        return episodeJTable;
    }

    /**
     * Builds the desktop panel.
     * @return  JPanel - the panel.
     */
    public JPanel buildDesktopPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        desktopPane = new JDesktopPane();
        desktopPane.setPreferredSize(new Dimension(300, 400));
        desktopPane.setMinimumSize(new Dimension(200, 200));
        desktopPane.setVisible(true);
        panel.add(desktopPane);

        return panel;
    }

    /**
     * Sets the frame to be shown or not.
     * @param show  boolean - true if to show, else false.
     */
    public void showFrame(boolean show) {
		radioInfoFrame.setVisible(show);
	}

    /**
     * Shows a message in a pane.
     * @param message   String - the message to be shown.
     */
	public void showMessage(String message) {
        JOptionPane.showMessageDialog(radioInfoFrame, message);
    }

    /**
     * Resets the channel table.
     */
	public void resetChannelTable() {
        channelTable.setRowCount(0);
        channelTable.fireTableDataChanged();
    }

    /**
     * Resets the episode table.
     */
	public void resetEpisodeTable() {
        episodeTable.setRowCount(0);
        episodeTable.fireTableDataChanged();
    }

    /**
     * Removes the frame that shows update progress.
     */
    public void disposeUpdateFrame() {
        updateFrame.dispose();
    }

    /**
     * Gets the frame that shows update progress.
     * @return  JInternalFrame - the frame.
     */
    public JInternalFrame getUpdateFrame() {
        return updateFrame;
    }

    /**
     * Updates the progress bar.
     * @param value int - value to update progress bar.
     */
    public void updateProgress(int value) {
        if (updateProgress.isShowing()) {
            updateProgress.setValue(value);
            /* If progress bar is full, remove update frame. */
            if (value >= updateProgress.getMaximum()) {
                disposeUpdateFrame();
            }
        }
    }

    /**
     * Creates and shows the update frame with progress bar.
     * @param totalValue    int - the highest value for progress bar.
     */
    public void createUpdateFrame(int totalValue) {
        updateFrame = new JInternalFrame("Updating lists...", false, false,
                                         false, false);

        updateProgress = new JProgressBar(0, totalValue);
        updateProgress.setValue(0);
        updateProgress.setStringPainted(true);

        int HEIGHT = 60;
        int WIDTH = 200;
        Dimension frameSize = new Dimension(WIDTH, HEIGHT);
        updateFrame.setSize(frameSize);
        updateFrame.add(updateProgress);
        desktopPane.add(updateFrame);
        updateFrame.setLocation((desktopPane.getWidth()/2) - (WIDTH/2),
                                (desktopPane.getHeight()/2) - (HEIGHT/2));
        updateFrame.moveToFront();
        updateFrame.setVisible(true);
    }

    /**
     * Sets the update menu item enabled or disabled.
     * @param enable    boolean - true to enable, false to disable.
     */
    public void enableUpdate(boolean enable) {
        updateItem.setEnabled(enable);
    }

    /**
     * Quits the program.
     */
    public void quitOption() {
        radioInfoFrame.dispatchEvent(new WindowEvent(radioInfoFrame,
                                     WindowEvent.WINDOW_CLOSING));
    }

    /**
     * Creates a frame with episode information and shows it in desktop.
     * @param title             String - the episode's title.
     * @param description       String - the episode's description.
     * @param imageURLstring    String - the episode's imageURL.
     * @param time              String - the time when the episode is running.
     */
    public void createEpisodeFrame(String title, String description,
                                   String imageURLstring, String time) {
        final JInternalFrame episodeFrame;
        episodeFrame = new JInternalFrame(title, true, true, true, true);
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createTitledBorder("Info"));
        infoPanel.setLayout(new BorderLayout());
        infoPanel.setPreferredSize(new Dimension(100, 100));
        infoPanel.setVisible(true);

        /* Set description. */
        JLabel descript;
        descript = new JLabel(description + " " + time);
        JScrollPane infoScroll = new JScrollPane(descript);
        infoPanel.add(infoScroll);
        episodeFrame.add(infoPanel, BorderLayout.NORTH);

        /* Set image if it exist. */
        if (imageURLstring != null) {
            URL imageURL;
            try {
                imageURL = new URL(imageURLstring);
                BufferedImage image = ImageIO.read(imageURL);
                int imageWidth = image.getWidth()/2;
                int imageHeight = image.getHeight()/2;
                JLabel imageLabel = new JLabel(new ImageIcon(image.
                                    getScaledInstance(imageWidth, imageHeight,
                                    Image.SCALE_DEFAULT)));
                JScrollPane imageScrollPane = new JScrollPane(imageLabel);
                episodeFrame.add(imageScrollPane, BorderLayout.CENTER);
            } catch (MalformedURLException e) {
                showMessage(e.toString());
            } catch (IOException e) {
                showMessage(e.toString());
            }
        }
        int HEIGHT = 300;
        int WIDTH = 300;
        Dimension frameSize = new Dimension(WIDTH, HEIGHT);
        episodeFrame.setSize(frameSize);
        episodeFrame.setMinimumSize(new Dimension(WIDTH/2, HEIGHT/2));
        episodeFrame.setVisible(true);
        desktopPane.add(episodeFrame);
        episodeFrame.moveToFront();
    }

    /**
     * Gets the channel's name from channel table.
     * @param row   int - the table row.
     * @param col   int - the table column.
     * @return  String - the name of channel.
     */
    public String getChannelName(int row, int col) {
        return channelJTable.getValueAt(row, col).toString();
    }

    /**
     * Gets the selected row in the channel table.
     * @return  int - the selected row.
     */
    public int getSelectedChannelRow() {
        return channelJTable.getSelectedRow();
    }

    /**
     * Gets the episode's name from episode table.
     * @param row   int - the table row.
     * @param col   int - the table column.
     * @return  String - the name of episode.
     */
    public String getEpisodeName(int row, int col) {
        return episodeJTable.getValueAt(row, col).toString();
    }

    /**
     * Gets the selected row in the episode table.
     * @return  int - the selected row.
     */
    public int getSelectedEpisodeRow() {
        return episodeJTable.getSelectedRow();
    }

    /**
     * Adds an channel to the channel table.
     * @param title         String - the channel's title.
     * @param channelType   String - the channel's type.
     */
    public void addChannelToTable(String title, String channelType) {
        channelTable.addRow(new Object[]{ title, channelType});
    }

    /**
     * Adds an episode to the episode table.
     * @param title     String - the episode's title.
     * @param startTime String - the starting time.
     * @param endTime   String - the ending time.
     */
    public void addEpisodeToTable(String title, String startTime,
                                  String endTime) {
        episodeTable.addRow(new Object[]{ title, startTime + " - " + endTime});
    }

    /**
     * Gets the amount of rows in channel table.
     * @return  int - the amount of rows.
     */
    public int getChannelRowCount() {
        return channelTable.getRowCount();
    }

    /**
     * Gets the amount of rows in episode table.
     * @return  int - the amount of rows.
     */
    public int getEpisodeRowCount() {
        return episodeTable.getRowCount();
    }
}
