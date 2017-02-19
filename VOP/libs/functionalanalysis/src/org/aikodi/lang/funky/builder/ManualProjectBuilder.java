package org.aikodi.lang.funky.builder;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.workspace.DocumentScanner;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.lang.funky.input.direct.DirectDocumentLoader;
import org.aikodi.lang.funky.input.direct.DirectDocumentScanner;

/**
 * A simple project builder for experimentation. Use it by adding
 * declarations directly to the model.
 * 
 * @author Marko van Dooren
 */
public class ManualProjectBuilder extends ProjectBuilder {

   /**
    * Create a new project builder that creates a project with the given name.
    * A single dummy document scanner is attached.
    * 
    * @param name The name of the project.
    * @throws ProjectException
    */
   public ManualProjectBuilder(String name) throws ProjectException {
      super(name);
      _scanner = createDocumentScanner();
      view().addSource(_scanner);
   }

   /**
    * "The" document scanner.
    */
   private DocumentScanner _scanner;

   /**
    * Add the given declaration to the root namespace.
    * 
    * @param declaration The declaration to be added.
    */
   public void add(Declaration declaration) {
      NamespaceDeclaration nsd = new DocumentFactory().createDocument(view());
      connect(declaration, nsd,"");
   }
   
   /**
    * The default implementation returns a {@link DirectDocumentScanner}.
    * 
    * @return A document scanner for the project.
    */
   protected DocumentScanner createDocumentScanner() {
      return new DirectDocumentScanner();
   }
   

   public void add(Declaration declaration, String namespaceFQN) {
      NamespaceDeclaration nsd = new DocumentFactory().createDocument(namespaceFQN,view());
      connect(declaration, nsd,namespaceFQN);
   }

   private void connect(Declaration declaration, NamespaceDeclaration nsd, String namespaceFQN) {
      nsd.add(declaration);
      Document document = nsd.nearestAncestor(Document.class);
      addDocument(namespaceFQN, document);
   }

   private void addDocument(String namespaceFQN, Document document) throws Error {
      DocumentLoaderNamespace ns = (DocumentLoaderNamespace) view().namespace().getOrCreateNamespace(namespaceFQN);
      DirectDocumentLoader loader = new DirectDocumentLoader(_scanner, document);
      try {
         ns.addDocumentLoader(loader);
      } catch (InputException e) {
         // Because we know that we provide a direct document loader, no exception
         // should be thrown. If it happens anyway, something is VERY wrong.
         throw new Error(e);
      }
   }
   
   public DocumentBuilder<ManualProjectBuilder> createInRootNamespace() {
      return new DocumentBuilder<ManualProjectBuilder>(view(), this, d -> {addDocument("", d);});
   }

   public DocumentBuilder<ManualProjectBuilder> createInNamespace(String namespaceFQN) {
      return new DocumentBuilder<ManualProjectBuilder>(namespaceFQN, view(), this, d -> {
         addDocument(namespaceFQN, d);
         });
   }
}

