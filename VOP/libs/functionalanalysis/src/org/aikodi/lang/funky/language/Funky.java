package org.aikodi.lang.funky.language;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.language.LanguageImpl;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.Defined;
import org.aikodi.chameleon.core.property.DynamicChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.chameleon.core.validation.namespace.MultipleDeclarationsWithSameName;
import org.aikodi.chameleon.support.modifier.PublicProperty;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.executors.Subject;
import org.aikodi.lang.funky.usecase.UseCase;

import be.kuleuven.cs.distrinet.rejuse.junit.BasicRevision;
import be.kuleuven.cs.distrinet.rejuse.junit.Revision;

import com.google.java.contract.Ensures;

/**
 * A language for functional analysis.
 * 
 * @author Marko van Dooren
 */
public class Funky extends LanguageImpl {

   /**
    * Create a new language object.
    */
   @Ensures({"name().equals(\"Funky\")"})
   public Funky() {
      this("Funky", new BasicRevision(0, 1, 0));
   }

   /**
    * Constants that denotes properties of elements in the
    * language for functional analysis.
    * 
    * Most of them will be used only after relations are added to the model.
    */
   public final StaticChameleonProperty INHERITABLE;
   public final StaticChameleonProperty OVERRIDABLE;
   public final ChameleonProperty REFINABLE;
   public final DynamicChameleonProperty DEFINED;
   public final ChameleonProperty FINAL;
   public final StaticChameleonProperty PUBLIC;

   /**
    * Create a new language for functional analysis with the given name
    * and version.
    * 
    * @param name The name of the language.
    * @param version The version of the language.
    */
   protected Funky(String name, Revision version) {
      super(name, version);
      INHERITABLE = new StaticChameleonProperty("inheritable", this, Declaration.class);
      OVERRIDABLE = new StaticChameleonProperty("overridable", this, Declaration.class);
      REFINABLE = new StaticChameleonProperty("refinable", this, Declaration.class);
      DEFINED = new Defined("defined", this);
      OVERRIDABLE.addImplication(INHERITABLE);
      OVERRIDABLE.addImplication(REFINABLE);
      FINAL = new StaticChameleonProperty("final", this, Declaration.class);
      PUBLIC = new PublicProperty(this, SCOPE_MUTEX);
   }

   /**
    * {@inheritDoc}
    * 
    * Adds a single object of each of the following rules:
    * <ul>
    *   <li>{@link DeclarationsPublicByDefault}</li>
    * </ul>
    */
   @Override
   protected void initializePropertyRules() {
      addPropertyRule(new DeclarationsPublicByDefault());
   }

   @Override
   protected void initializeValidityRules() {
      addValidityRule(new MultipleDeclarationsWithSameName(Namespace.class, UseCase.class, Actor.class, Subject.class));
   }
}