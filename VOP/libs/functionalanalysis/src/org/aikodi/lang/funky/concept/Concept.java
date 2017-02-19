package org.aikodi.lang.funky.concept;

import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.util.association.Single;
import org.aikodi.lang.funky.behavior.description.Description;

import java.util.ArrayList;
import java.util.List;

public class Concept extends CommonDeclaration implements DeclarationContainer {

    private Single<Description> definition = new Single<>(this,true);

    //FIXME change back to Multi when attributes are real things
    private List<NameReference<? extends Attribute>> attributes = new ArrayList<>();

    /**
     * Default constructor for a Concept
     * <p>
     * The concept will have an empty list of attributes, definition and name.
     *
     * @see SimpleNameSignature
     */
    public Concept() {
        super(new SimpleNameSignature(""));
    }

    /**
     * A constructor using the Concept's name
     * <p>
     * The concept will have an empty list of attributes and definition.
     *
     * @see SimpleNameSignature
     */
    public Concept(String name) {
        super(new SimpleNameSignature(name));
        setName(name);
    }

    /**
     * A constructor using the Concept's name and a definition.
     * <p>
     * The concept will have an empty list of attributes
     *
     * @param name the name of this Concept
     * @param definition the definition of this Concept
     * @see SimpleNameSignature
     */
    public Concept(String name, Description definition) {
        this(name, definition, new ArrayList<>());
    }

    /**
     * A constructor using the Concept's name, a definition and a list of
     * attributes.
     *
     * @param name the name of this Concept
     * @param definition the definition of this Concept
     * @param attributes the list of attributes of this Concept
     * @see SimpleNameSignature
     */
    public Concept(String name, Description definition, List<String> attributes) {
        // Make a legit Declaration of this
        super(new SimpleNameSignature(name));

        if (name == null || definition == null){
            throw new IllegalArgumentException();
        }

        // Set the name variable
        setName(name);

        setDefinition(definition);
        setAttributes(attributes);
    }

    @Override
    public List<? extends Declaration> locallyDeclaredDeclarations() {
        return descendants(Declaration.class);
    }

    @Override
    protected Element cloneSelf() {
        List<String> attributes = new ArrayList<>();
        for (NameReference ref : getAttributeReferences()){
            attributes.add(ref.name());
        }
        return new Concept(getName(), getDefinition(), attributes);
    }

    /**
     * A getter for the name of this Concept.
     *
     * @return name of the Concept
     */
    public String getName() {
        return name();
    }

    /**
     * A setter for the name of this Concept.
     *
     * @param name the name of this Concept
     */
    @Override
    public void setName(String name) {
        super.setName(name);
    }

    /**
     * A getter for the definition of this Concept.
     *
     * @return definition of the Concept
     */
    public Description getDefinition() {
        return definition.getOtherEnd();
    }

    /**
     * A setter for the definition of this Concept.
     *
     * @param definition the definition of this Concept
     */
    public void setDefinition(Description definition) {
        if (definition == null){
            throw new IllegalArgumentException();
        }

        set(this.definition, definition);
    }

    /**
     * Gets the references to all attributes of this concept
     * @return list of references to attributes
     */
    public List<NameReference<? extends Attribute>> getAttributeReferences() {
        return attributes;
    }

    /**
     * Gets the attributes of this concept
     * @return list of attributes
     * @throws LookupException thrown if a reference is invalid
     */
    public List<Attribute> getAttributes() throws LookupException {
        List<Attribute> result = new ArrayList<>(attributes.size());
        for (NameReference<? extends Attribute> attribute : getAttributeReferences()) {
            result.add(attribute.getElement());
        }
        return result;
    }

    /**
     * A setter for the list of attributes of this Concept.
     *
     * @param list a list of attributes of this Concept
     */
    public void setAttributes(List<String> list) {
        for (String s : list){
            //FIXME uncomment when attributes are fully implemented
            //add(attributes, new NameReference<>(s, Attribute.class));

            attributes.add(new NameReference<>(s, Attribute.class));
        }
    }

    /**
     * Formats all data of this Concept into a readable format.
     *
     * @return the string representation of this Concept
     */
    @Override
    public String toString() {
        return "Concept{" +
                "name='" + name() + "'\'" +
                ", definition='" + definition + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}