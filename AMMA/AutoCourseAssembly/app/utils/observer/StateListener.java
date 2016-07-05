package utils.observer;

import java.util.EventListener;

/**
 * @author Titouan Vervack
 */
public interface StateListener extends EventListener {

    void stateChanged(StateChangedEvent e);

}
