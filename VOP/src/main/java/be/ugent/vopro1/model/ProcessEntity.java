package be.ugent.vopro1.model;

import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.lang.funky.process.Process;
import org.aikodi.lang.funky.usecase.UseCase;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Wrapper around a {@link org.aikodi.lang.funky.process.Process} that allows
 * setting a priority and a name.
 */
public class ProcessEntity extends Process {

    private int priority;

    /**
     * No-args constructor with temporary name
     */
    public ProcessEntity() {
        super("temp");
    }

    /**
     * Creates a new ProcessEntity with given name
     *
     * @param name Name of the process
     * @see org.aikodi.chameleon.core.declaration.CommonDeclaration#CommonDeclaration(Signature)
     */
    public ProcessEntity(String name) {
        super(name);
    }

    /**
     * Formats all data of this ProcessEntity into a readable format.
     *
     * @return the string representation of this ProcessEntity
     */
    @Override
    public String toString() {
        String useCases = "";
        try {
            useCases = ", usecases='" + useCases().stream().map(UseCase::toString).collect(Collectors.joining(", ")) + "'";
        } catch (LookupException e) {
            e.printStackTrace();
        }

        return "ProcessEntity{"
                + "name='" + name() + "'"
                + ", priority='" + priority + "'"
                + useCases
                + '}';
    }

    /**
     * A getter for the priority of a process
     *
     * @return The priority of a process
     */
    public int getPriority() {
        return priority;
    }

    /**
     * A setter for the priority of a process
     *
     * @param priority The priority of a process
     */
    public void setPriority(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException("Priority should be larger than zero.");
        }

        this.priority = priority;
    }

    /**
     * A setter for the use cases of a process
     *
     * @param useCases The use cases of a process
     */
    public void setUseCases(List<String> useCases) {
        if (useCases == null || useCases.contains(null)) {
            throw new IllegalArgumentException("List of concepts cannot be, or contain, null");
        }

        for (String useCase : useCases){
            this.addUseCase(new NameReference<>(useCase, UsecaseEntity.class));
        }
    }

    /**
     * Provides a Builder for {@link ProcessEntity}.
     */
    public static class ProcessEntityBuilder {

        private String name;
        private int priority;
        private List<String> useCases;

        /**
         * Creates a new ProcessEntityBuilder
         *
         * @return A new ProcessEntityBuilder
         */
        public static ProcessEntityBuilder aProcessEntity() {
            return new ProcessEntityBuilder();
        }

        /**
         * Sets the name
         *
         * @param name Name to set
         * @return the builder
         */
        public ProcessEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the priority
         *
         * @param priority Priority to set
         * @return the builder
         */
        public ProcessEntityBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Sets the usecases
         *
         * @param useCases UseCases to set
         * @return the builder
         */
        public ProcessEntityBuilder useCases(List<String> useCases) {
            this.useCases = useCases;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances
         *
         * @return a new ProcessEntityBuilder with the same values as the current
         * one
         */
        public ProcessEntityBuilder but() {
            return aProcessEntity().name(name).priority(priority).useCases(useCases);
        }

        /**
         * Creates a ProcessEntity with the current values
         *
         * @return ProcessEntity with the current values
         * @see ProcessEntity
         */
        public ProcessEntity build() {
            ProcessEntity processEntity = new ProcessEntity(name);
            processEntity.setPriority(priority);
            processEntity.setUseCases(useCases);

            return processEntity;
        }
    }
}
