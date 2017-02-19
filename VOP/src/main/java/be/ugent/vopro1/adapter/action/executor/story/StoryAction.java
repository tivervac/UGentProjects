package be.ugent.vopro1.adapter.action.executor.story;

import be.ugent.vopro1.adapter.action.Action;

/**
 * The representation of a story creation.
 *
 * @see Action
 */
public class StoryAction extends Action {

    private int numWhile;
    private int numRepeat;
    private int numBranch;

    /**
     * A getter for the maximum number of times a while loop should be repeated
     * for generating this story, as defined in the JSON body.
     *
     * @return the maximum number of times a while loop should be repeated for
     * generating this story
     */
    public int getNumWhile() {
        return numWhile;
    }

    /**
     * A setter for the maximum number of times a while loop should be repeated
     * for generating this story, as defined in the JSON body.
     *
     * @param numWhile the maximum number of times a while loop should be
     * repeated for generating this story
     */
    public void setNumWhile(int numWhile) {
        this.numWhile = numWhile;
    }

    /**
     * A getter for the maximum number of times a repeat loop should be repeated
     * for generating this story, as defined in the JSON body.
     *
     * @return the maximum number of times a repeat loop should be repeated for
     * generating this story
     */
    public int getNumRepeat() {
        return numRepeat;
    }

    /**
     * A setter for the maximum number of times a repeat loop should be repeated
     * for generating this story, as defined in the JSON body.
     *
     * @param numRepeat the maximum number of times a repeat loop should be
     * repeated for generating this story
     */
    public void setNumRepeat(int numRepeat) {
        this.numRepeat = numRepeat;
    }

    /**
     * A getter for the maximum number of times a branch should be taken
     * for generating this story, as defined in the JSON body.
     *
     * @return the maximum number of times a branch should be taken for
     * generating this story
     */
    public int getNumBranch() {
        return numBranch;
    }

    /**
     * A setter for the maximum number of times a branch should be taken
     * for generating this story, as defined in the JSON body.
     *
     * @param numBranch the maximum number of times a branch should be taken
     * repeated for generating this story
     */
    public void setNumBranch(int numBranch) {
        this.numBranch = numBranch;
    }
}
