package org.aikodi.lang.funky.builder;

import java.util.function.Consumer;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.DemandImport;
import org.aikodi.chameleon.core.namespacedeclaration.DirectImport;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.reference.NameReference;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.lang.funky.executors.Actor;
import org.aikodi.lang.funky.executors.Subject;
import org.aikodi.lang.funky.usecase.UseCase;

public class DocumentBuilder<P> extends Builder<P, Document>{

   protected NamespaceDeclaration _namespaceDeclaration;
   
   public DocumentBuilder(View view, P parent, Consumer<Document> consumer) {
      super(parent, consumer);
      _namespaceDeclaration = new DocumentFactory().createDocument(view);
      consumer().accept(_namespaceDeclaration.nearestAncestor(Document.class));
   }

   public DocumentBuilder(String namespaceFQN, View view, P parent, Consumer<Document> consumer) {
      super(parent, consumer);
      _namespaceDeclaration = new DocumentFactory().createDocument(namespaceFQN, view);
      consumer().accept(_namespaceDeclaration.nearestAncestor(Document.class));
   }

   public UseCaseBuilder<DocumentBuilder<P>> usecase(String name) {
      return usecase(name, u -> {});
   }
   public UseCaseBuilder<DocumentBuilder<P>> usecase(String name, Consumer<? super UseCase> peeker) {
        return new UseCaseBuilder<DocumentBuilder<P>>(name, this, u -> {
           _namespaceDeclaration.add(u);
           peeker.accept(u);
        });
   }
   
   public DocumentBuilder<P> actor(String name) {
      _namespaceDeclaration.add(new Actor(name));
      return this;
   }
   
   public DocumentBuilder<P> subject(String name) {
      _namespaceDeclaration.add(new Subject(name));
      return this;
   }
   
   public DocumentBuilder<P> addImport(String fullyQualifiedName) {
      NameReference<Declaration> nameReference = new NameReference<Declaration>(fullyQualifiedName, Declaration.class);
      DirectImport<Declaration> directImport = new DirectImport<Declaration>(nameReference, Declaration.class);
      _namespaceDeclaration.addImport(directImport);
      return this;
   }

   public DocumentBuilder<P> addDemandImport(String fullyQualifiedName) {
      NameReference<Namespace> nameReference = new NameReference<Namespace>(fullyQualifiedName, Namespace.class);
      DemandImport directImport = new DemandImport(nameReference);
      _namespaceDeclaration.addImport(directImport);
      return this;
   }
   
   public P endDocument() {
      return parent();
   }
}
