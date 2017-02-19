package org.aikodi.lang.funky.virtualmachine;

import org.aikodi.lang.funky.behavior.AbstractStep;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.conditions.Condition;

public interface VirtualProcess {
    /**
     * Advances the execution with a given node
     * @param behavior node to continue from
     */
    void advance(Behavior behavior);

    /**
     * Advances the execution with a given node
     * Checks a condition to determine whether to execute special behavior
     * @param behavior behavior to continue from
     * @param condition condition to continue from this behavior
     * @param source the previous behavior node
     * @return
     */
    boolean advance(Behavior behavior, Condition condition, Behavior source);

    /**
     * Handles a condition
     * @param c condition to handle
     */
    void handleCondition(Condition c);

    /**
     * Handles a step
     * @param s step to handle
     */
    void handleStep(AbstractStep s);

    /**
     * Stops the execution and gives the story to its host
     */
    void terminate();
}
