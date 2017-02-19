package be.ugent.vopro1.adapter.action.executor.copy;

import be.ugent.vopro1.adapter.action.Action;

/**
 * The representation of a usecase copy.
 *
 * @see Action
 */
public class CopyAction extends Action {

    private String destination;

    /**
     * A getter for the destination of the copy, as defined in the JSON body.
     *
     * @return the destination of the copy
     */
    public String getDestination() {
        return destination;
    }

    /**
     * A setter for the destination of the copy, as defined in the JSON body.
     *
     * @param destination the destination of the copy, as defined in the JSON
     * body
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }
}
