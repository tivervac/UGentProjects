package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.model.ProcessEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * PresentationModel of a {@link ProcessEntity}
 */
public class ProcessPresentationModel extends PresentationModel {

    private ProcessEntity entity;

    @Override
    public void setEntity(Object entity) {
        this.entity = (ProcessEntity) entity;
    }

    @Override
    public String getIdentifier() {
        return entity.name();
    }

    /**
     * Retrieves the useCases part of the process using {@link ProcessEntity#getUseCases()}
     *
     * @return List of use cases
     */
    public List<String> getUseCases() {
        return entity.getUseCases();
    }

    /**
     * Retrieves the name of the process using {@link ProcessEntity#name()}
     *
     * @return Name of the process
     */
    public String getName() {
        return entity.name();
    }

    /**
     * Retrieves the priority of the process using {@link ProcessEntity#getPriority()}
     *
     * @return Priority of the process
     */
    public int getPriority() {
        return entity.getPriority();
    }
}
