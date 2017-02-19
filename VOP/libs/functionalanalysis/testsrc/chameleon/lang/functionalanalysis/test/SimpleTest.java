package chameleon.lang.functionalanalysis.test;

import static org.junit.Assert.assertSame;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.util.Box;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.lang.funky.behavior.LabeledBehavior;
import org.aikodi.lang.funky.builder.ManualProjectBuilder;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.executors.Subject;
import org.aikodi.lang.funky.usecase.UseCase;
import org.junit.Before;
import org.junit.Test;

import be.kuleuven.cs.distrinet.rejuse.action.Action;

import java.util.HashMap;
import java.util.List;

/**
 * This is a very primitive test case, and <b>NOT</b> an example of a good
 * test suite.
 * 
 * @author Marko van Dooren
 */
public class SimpleTest {

   private Box<UseCase> _first = new Box<>();
   private Box<LabeledBehavior> _first_x = new Box<>();
   private Box<LabeledBehavior> _first_y = new Box<>();
   
   public void createFirstUseCase() {
      String user = "Client";
      String system = "THE system";
      String sandra = "actors.Bullock";
      builder()
        .createInRootNamespace()
        .usecase("useless case", u -> {
           _first.set(u);
           })
          .primaryActor(user)
          .primaryActor(sandra)
          .precondition().text("The sky hasn't fallen.")
          .postcondition().text("The sky will be falling")
          .normalFlow()
            .step("Hello",user)
            .step("The user hits the screen.",user)
              .openBlock()
                .label("x",l ->{_first_x.set(l);}).step("Nested step",system)
                .alternate("Alternative").label("y",l ->{_first_y.set(l);}).openBlock()
                   .step("More nesting.",user)
                   .endBlock()
                .endBlock()
              .step("After nesting",system)
              .doWhile()
                 .condition().text("I am not tired")
                 .openBlock()
                   .step("Run.",user)
                 .endBlock()
              .step("float in space", sandra)
            .endBlock()
          .alternateFlow("Alternative")
            .when().text("The end is near")
            .execute()
              .step("Do something else",user)
              .returnTo("x",system)
              .endBlock();
   }
   
   public void createSecondUseCase() {
      builder()
        .createInNamespace("accounting")
        .addImport("actors.Bullock")
        .usecase("HelloAccountant")
        .primaryActor("Bullock")
        .postcondition().text("Hello has been said.")
          .normalFlow()
            .step("Say hello", "Bullock");
      
   }
   
   public void createThirdUseCase() {
      builder()
        .createInNamespace("accounting")
        .addDemandImport("actors")
        .usecase("MoreHelloAccountant")
        .primaryActor("Bullock")
        .postcondition().text("Hello has been said.")
          .normalFlow()
            .alternate("alternative").label("q").step("Say hello", "Bullock")
          .endBlock()
          .alternateFlow("alternative")
          .when().text("The accountant is not present.")
          .execute()
           .step("Say goodbye", "Bullock")
           .returnTo("q", "THE system");
   }

   public void createSandraBullock() {
      builder()
        .createInNamespace("actors")
        .actor("Bullock");
   }

   public void createClient() {
      builder()
        .createInRootNamespace()
        .actor("Client");
   }
   
   private RootNamespace _root;
   private ManualProjectBuilder _builder;
   
   private ManualProjectBuilder builder() {
      return _builder;
   }
   
   @Before
   public void setup() {
      try {
         _builder = new ManualProjectBuilder("test");
         _root = _builder.view().namespace();
         createFirstUseCase();
         createSecondUseCase();
         createThirdUseCase();
         createClient();
         createSandraBullock();
         Subject system = createSubject();
         system.addUseCaseReference(new NameReference<UseCase>("useless case",UseCase.class));
         builder().add(system);
      } catch (ProjectException e) {
         e.printStackTrace();
      }
   }

   private Subject createSubject() {
      Subject result = new Subject("THE system");
      return result;
   }
   
   @Test
   @SuppressWarnings("unused")
   public void test() throws ModelException {
      UseCase uc = _root.find("useless case", UseCase.class);
      uc.scope();
      assertSame(uc, _first.get());
      Actor sandra = _root.find("actors.Bullock", Actor.class);
      assertSame(uc.primaryActors().get(1),sandra);
   }

   /**
    * Test whether all cross-reference in the model are valid.
    * 
    * @throws LookupException
    */
   @Test
   public void resolveAllCrossReference() throws LookupException {
      _root.apply(new Action<CrossReference, LookupException>(CrossReference.class) {
         @Override
         protected void doPerform(CrossReference cref) throws LookupException {
            cref.getElement();
         }
      });
   }
   
   @Test
   public void testVerification() {
     assertSame(_root.verify(), Valid.create()); 
   }


}
