import javax.swing.SwingUtilities;

/**
 * Program for getting and showing information about radio channels and their
 * episodes from a given xml path.
 * @author Johan Ahlqvist
 * 2017-12-04
 */
public class RadioInfo {

    private static String title = "RadioInfo";
    private static String xmlAddress = "http://api.sr.se/api/v2/channels";

	public static void main(String[] args) {
		
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                RadioInfoController c = new RadioInfoController(xmlAddress);
                RadioInfoFrame gui = new RadioInfoFrame(title, c);
                c.setGui(gui);
                gui.showFrame(true);
                c.createInfoUpdater();
            }});

    }
}


