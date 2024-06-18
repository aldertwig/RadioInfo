import org.xml.sax.SAXException;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Class that represents a swing worker.
 * @author Johan Ahlqvist
 * 2017-12-04
 */
public class RadioInfoUpdateWorker extends SwingWorker<Boolean, Integer> {

    private static RadioInfoController controller;  //The controller.

    /**
     * Constructor for swing worker. Takes a controller as parameter.
     * @param controller    RadioInfoController - the controller.
     */
    public RadioInfoUpdateWorker(RadioInfoController controller) {
        this.controller = controller;

    }

    @Override
    protected Boolean doInBackground() throws Exception {
        controller.setIsUpdating(true);

        try {
            controller.getXMLParser().getRadioInfo(controller, this);
        } catch (IOException e) {
            controller.guiShowMessage(e.toString());
            return false;
        } catch (SAXException e) {
            controller.guiShowMessage(e.toString());
            return false;
        } catch (ParserConfigurationException e) {
            controller.guiShowMessage(e.toString());
            return false;
        }
        return true;
    }

    public void createUpdateProgress(int totalValue) {
        controller.createProgressUpdate(totalValue);
    }

    public void updateProgress(int value) {
        publish(value);
    }

    @Override
    protected void process(List<Integer> chunks) {
        int index = (chunks.size() - 1);
        int returnValue = chunks.get(index);
        controller.showProgressUpdate(returnValue);
    }

    @Override
    protected void done() {
        try {
            Boolean returnValue = get();
            controller.updateDone();
        } catch (InterruptedException e) {
            controller.guiShowMessage(e.toString());
        } catch (ExecutionException e) {
            controller.guiShowMessage(e.toString());
        }
        controller.setIsUpdating(false);
    }
}
