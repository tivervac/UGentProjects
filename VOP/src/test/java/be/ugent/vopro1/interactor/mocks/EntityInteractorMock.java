package be.ugent.vopro1.interactor.mocks;

import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.interactor.entity.EntityInteractor;
import be.ugent.vopro1.interactor.entity.RefactorStrategy;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.util.RuntimeIOException;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.lang.funky.executors.Actor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EntityInteractorMock<E extends Declaration> implements EntityInteractor<E> {

    private IOException throwing = null;
    private RuntimeException throwingRun = null;
    private ConverterFacade converter;

    public EntityInteractorMock() {
        converter = ConverterFactory.getInstance();
    }

    @Override
    public E add(String projectName, E t) {
        if (t.name().equals("null")) throw new RuntimeIOException("name empty", null);
        maybeThrowRuntime();
        return t;
    }

    @Override
    public boolean exists(String projectName, E t) {
        maybeThrowRuntime();
        return false;
    }

    @Override
    public void remove(String projectName, E t) {
        if (t.name().equals("null")) throw new RuntimeException("name empty", null);
        maybeThrowRuntime();
    }

    @Override
    public E get(String projectName, E t) {
        maybeThrowRuntime();
        if (t.name().equals("null")) throw new RuntimeIOException("name empty", null);
        return t;
    }

    @Override
    public E edit(String projectName, E old, E t, RefactorStrategy refactorStrategy) {
        maybeThrowRuntime();
        if (t.name().equals("null")) throw new RuntimeIOException("name empty", null);
        return t;
    }

    @Override
    public List<E> getAll(String projectName, String type) {
        maybeThrowRuntime();
        List<E> result = new ArrayList<>();

        result.add((E) new Actor("actor"));
        return result;
    }

    @Override
    public List<E> find(String projectName, String query, String type) {
        maybeThrowRuntime();
        return getAll(projectName, type);
    }

    @Override
    public List<User> getAnalysts(String projectName, String name) {
        maybeThrowRuntime();
        return new ArrayList<>();
    }

    @Override
    public void addAnalyst(String projectName, String name, int userId) {
        maybeThrowRuntime();
    }

    @Override
    public void removeAnalyst(String projectName, String name, int userId) {
        maybeThrowRuntime();
    }

    private void maybeThrowRuntime() {
        if (throwingRun != null) {
            throw throwingRun;
        }
    }

    /**
     * Set an exception that the next call to the mock interactor should throw
     * @param ex Exception to throw next
     */
    public void setThrowing(IOException ex) {
        this.throwingRun = null;
        this.throwing = ex;
    }

    /**
     * Set an exception that the next call to the mock interactor should throw
     * @param ex Exception to throw next
     */
    public void setThrowing(RuntimeException ex) {
        this.throwing = null;
        this.throwingRun = ex;
    }
}
