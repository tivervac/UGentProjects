package org.aikodi.lang.funky.behavior.description;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.declaration.CommonDeclaration;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.util.association.Single;

public class ReferenceDescription extends ElementImpl implements Description  {
    private Single<Description> descriptionLeft = new Single<>(this,true);
    private Single<Description> descriptionRight = new Single<>(this,true);
    private Single<NameReference<? extends  CommonDeclaration>> reference = new Single<>(this, true);

    /**
     * Create a new step with the given description.
     *
     * @param description1 A description of the behavior of this step, to the left of the concept.
     * @param reference name of the declaration object
     * @param description2 A description of the behavior of this step, to the right of the concept.
     */
    @Ensures({"description() == description"})
    public ReferenceDescription(Description description1,
                       NameReference<? extends CommonDeclaration> reference,
                       Description description2) {
        setReference(reference);
        setDescriptionLeft(description1);
        setDescriptionRight(description2);
    }

    /**
     * Just for cloning.
     */
    protected ReferenceDescription() {
    }

    /**
     * Set the description part before the concept.
     *
     * @param description A description of the behavior of this step.
     */
    @Ensures({"description() == description"})
    public void setDescriptionLeft(Description description) {
        set(descriptionLeft, description);
    }

    /**
     * @return The description part before the concept.
     */
    public Description getDescriptionLeft() {
        return descriptionLeft.getOtherEnd();
    }

    /**
     * Set the description part before the concept.
     *
     * @param description A description of the behavior of this step.
     */
    @Ensures({"description() == description"})
    public void setDescriptionRight(Description description) {
        set(descriptionRight, description);
    }

    /**
     * @return The description part before the concept.
     */
    public Description getDescriptionRight() {
        return descriptionRight.getOtherEnd();
    }

    public void setReference(NameReference<? extends CommonDeclaration> reference){
        set(this.reference, reference);
    }

    public NameReference<? extends CommonDeclaration> getReference(){
        return reference.getOtherEnd();
    }

    @Override
    protected ReferenceDescription cloneSelf() {
        return new ReferenceDescription();
    }

    @Override
    public String text() {
        return getDescriptionLeft().text() + " " + getReference().name() + " " + getDescriptionRight().text();
    }
}
