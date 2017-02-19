package org.aikodi.lang.funky.concept;

import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;

/**
 * Class representing an attribute of a Concept
 * Stub
 */
public class Attribute extends CommonDeclaration{
    /**
     * Creates an Attribute from a String
     * @param name
     */
    public Attribute(String name){
        super(new SimpleNameSignature(name));
    }

    @Override
    protected Element cloneSelf() {
        return new Attribute(name());
    }
}
