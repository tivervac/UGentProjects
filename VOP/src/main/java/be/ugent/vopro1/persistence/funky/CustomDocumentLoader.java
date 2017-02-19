package be.ugent.vopro1.persistence.funky;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.persistence.EntityDAO;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.workspace.DocumentLoaderImpl;
import org.aikodi.chameleon.workspace.DocumentScanner;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.lang.funky.builder.DocumentFactory;

/**
 * Our custom document loader, based on the {@link DocumentLoaderImpl} from
 * chameleon.
 * <p>
 * The custom document loader uses our own {@link JsonConverter} and
 * {@link EntityDAO} to retrieve {@link PersistentObject} objects from the
 * database.
 *
 * @see DocumentLoaderImpl
 * @see JsonConverter
 * @see EntityDAO
 * @see PersistentObject
 */
public class CustomDocumentLoader extends DocumentLoaderImpl {

    private ConverterFacade converter;

    // Two identifiers, finding/removing
    private int id;

    // Database Connection
    private EntityDAO dao;

    // Loader view
    private View view;

    /**
     * Creates a new CustomDocumentLoader with given scanner, id and DAO.
     *
     * @param scanner DocumentScanner to retrieve a view from
     * @param id Unique identifier of the document to load
     * @param dao DAO to use for retrieval of the document
     * @see JsonConverter
     * @see JsonConverterFactory
     */
    public CustomDocumentLoader(DocumentScanner scanner, int id, EntityDAO dao) {
        scanner.add(this);
        this.id = id;
        this.dao = dao;
        this.view = scanner.view();

        converter = ConverterFactory.getInstance();

        try {
            refresh();
        } catch (InputException e) {
            throw new RuntimeException("Caught an InputException while initializing the document loader", e);
        }
    }

    /**
     * Used to determine which loader to remove from the scanner.
     *
     * @return the id of the document that this loader has to load.
     */
    public int getDocumentId() {
        return id;
    }

    /**
     * The method to call on a refresh.
     * <p>
     * This method keeps the Document that has to be loaded by this loader,
     * up-to-date.
     *
     * @see PersistentObject
     */
    @Override
    protected void doRefresh() throws InputException {
        // Get rid of what we had
        if (rawDocument() != null) {
            rawDocument().disconnect();
        }

        // Get the object assigned to this loader
        PersistentObject object = dao.getById(id);


        // Need converters here
        NamespaceDeclaration ns = new DocumentFactory().createDocument(view);

        try {
            // Risky cast
            Declaration ac = converter.convert(object);

            ns.add(ac);
            Document doc = new Document(ns);
            setDocument(doc);
        }  catch (ClassCastException e){
            throw new RuntimeException("Trying to load an object of incompatible type into funky", e);
        }
    }
}
