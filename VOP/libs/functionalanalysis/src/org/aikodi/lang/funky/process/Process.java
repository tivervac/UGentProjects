package org.aikodi.lang.funky.process;

import org.aikodi.chameleon.core.declaration.*;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.lang.funky.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

/**
 * A abstract class for processes.
 */
public class Process extends CommonDeclaration implements DeclarationContainer {

    private Multi<CrossReference<? extends UseCase>> _useCases = new Multi<>(this, 1, -1, "use cases");

    public Process(String name) {
        super(new SimpleNameSignature(name));
    }

    @Override
    protected Element cloneSelf() {
        return new Process(name());
    }

    public List<UseCase> useCases() throws LookupException {
        List<UseCase> result = new ArrayList<>(_useCases.size());
        for (CrossReference<? extends UseCase> useCase : useCaseReferences()) {
            result.add(useCase.getElement());
        }
        return result;
    }

    public void addUseCase(CrossReference<? extends UseCase> useCaseReference) {
        add(_useCases, useCaseReference);
    }

    public List<CrossReference<? extends UseCase>> useCaseReferences() {
        return _useCases.getOtherEnds();
    }

    public List<String> getUseCases() {
        List<String> result = new ArrayList<>();
        for (CrossReference ref : useCaseReferences()){
            NameReference nameReference = (NameReference) ref;
            result.add(nameReference.name());
        }
        return result;
    }

    @Override
    public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
        return descendants(Declaration.class);
    }
}
