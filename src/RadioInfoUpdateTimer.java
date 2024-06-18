import java.util.TimerTask;

/**
 * Class that represents the timer task that updates information on a timer.
 * @author Johan Ahlqvist
 * 2017-12-04
 */
public class RadioInfoUpdateTimer extends TimerTask {
	
    private static RadioInfoController controller;  //The controller.

    /**
     * Constructor for a new timer task.
     * @param controller    RadioInfoController - the controller.
     */
	public RadioInfoUpdateTimer(RadioInfoController controller){
		this.controller = controller;
	}

	@Override
	public void run() {
		if (!controller.isUpdating()) {
		    controller.runInfoUpdater();
        }
	}
}
