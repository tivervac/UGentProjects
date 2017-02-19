package be.ugent.vopro1.interactor.entity;

/**
 * Strategy pattern to abstract the refactoring
 * Always pass one to the Entity interactor when editing
 */
public interface RefactorStrategy {
    /**
     * Prepares the refactoring
     * Should be called before any changes to the data
     * This should collect all the references to the entity that's about to change
     */
    void prepare();

    /**
     * Executes the refactoring
     * Will not work if prepare hasn't been called prior
     */
    void execute();
}
