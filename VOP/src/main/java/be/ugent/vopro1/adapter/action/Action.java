package be.ugent.vopro1.adapter.action;

/**
 * A superclass for all actions that will rely on the Executor-interface.
 *
 * @see be.ugent.vopro1.adapter.action.executor.Executor
 */
public class Action {

    private String action;

    /**
     * A getter for the textual representation of this action.
     *
     * @return the textual representation of this action
     */
    public String getAction() {
        return action;
    }

    /**
     * A setter for the textual representation of this action.
     *
     * @param action the textual representation of this action
     */
    public void setAction(String action) {
        this.action = action;
    }
}
