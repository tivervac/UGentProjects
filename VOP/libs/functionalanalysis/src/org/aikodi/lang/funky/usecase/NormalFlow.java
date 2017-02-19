package org.aikodi.lang.funky.usecase;

import com.google.java.contract.Ensures;
import org.aikodi.chameleon.core.declaration.SimpleNameSignature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.lang.funky.behavior.Behavior;
import org.aikodi.lang.funky.conditions.Condition;

/**
 * The normal flow in a use case
 * Used to conform to the API
 */
public class NormalFlow extends AbstractFlow {
    /**
     * Create a new normal flow with the given name, condition, and behavior.
     *
     * @param name The name of the alternate flow.
     * @param condition The condition under which the alternate flow is executed.
     * @param behavior The behavior of the alternate flow.
     */
    @Ensures({"name().equals(name)",
            "condition() == condition",
            "behavior() == behavior"})
    public NormalFlow(String name, Condition condition, Behavior behavior) {
        super(new SimpleNameSignature(name),condition, behavior);
    }

    /**
     * Create a new normal flow with the given signature, condition, and behavior.
     *
     * @param signature The signature of the alternate flow.
     * @param condition The condition under which the alternate flow is executed.
     * @param behavior The behavior of the alternate flow.
     */
    @Ensures({"signature() == signature",
            "condition() == condition",
            "behavior() == behavior"})
    public NormalFlow(SimpleNameSignature signature, Condition condition, Behavior behavior) {
        super(signature,condition,behavior);
    }

    @Override
    protected Element cloneSelf() {
        return new NormalFlow((SimpleNameSignature)null,null,null);
    }

    @Override
    public boolean isSuccessful(){
        return true;
    }
}
