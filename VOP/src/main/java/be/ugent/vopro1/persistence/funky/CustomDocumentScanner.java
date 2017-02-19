package be.ugent.vopro1.persistence.funky;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import org.aikodi.chameleon.workspace.*;

/**
 * Our custom document scanner, based on the {@link DocumentScannerImpl} from
 * chameleon.
 * <p>
 * The custom document scanner is able to hold mutiple DocumentLoaderImpl's.
 *
 * @see DocumentScannerImpl
 * @see org.aikodi.chameleon.workspace.DocumentLoaderImpl
 */
public class CustomDocumentScanner extends DocumentScannerImpl {

    private EntityDAO dao;
    private String projectName;

    /**
     * Creates a new CustomDocumentScanner.
     *
     * @param projectName name of the project this scanner is used for
     */
    public CustomDocumentScanner(String projectName) {
        dao = DAOProvider.get("entity");
        this.projectName = projectName;
    }

    /**
     * Returns a unique label for this DocumentScanner.
     *
     * @return Label for this DocumentScanner
     */
    @Override
    public String label() {
        return "VOPRO Persistent Object Scanner";
    }

    /**
     * Add a new DocumentLoader to this scanner.
     *
     * @param id Unique identifier of the model to add
     * @see CustomDocumentLoader
     */
    public void addToModel(int id) {
        // Add a new DocumentLoader to the list of loaders
        this.add(new CustomDocumentLoader(this, id, dao));
    }

    /**
     * Remove a DocumentLoader from this scanner.
     *
     * @param id The id of the DocumentLoader that needs to be removed
     * @see CustomDocumentLoader
     */
    public void removeFromModel(int id) {
        // Inefficient, but only way to remove a loader?
        // Iterate over loaders until we have a matching document id
        for (DocumentLoader l : documentLoaders()) {
            CustomDocumentLoader loader = (CustomDocumentLoader) l;
            if (loader.getDocumentId() == id) {
                // Clears relevant namespace and calls disconnect()
                this.remove(l);
            }
        }
    }

    /**
     * Refreshes a document
     * @param id id of the DocumentLoader corresponding to this document
     * @throws InputException if refreshing fails
     */
    public void refreshLoader(int id) throws InputException {
        for (DocumentLoader l : documentLoaders()) {
            CustomDocumentLoader loader = (CustomDocumentLoader) l;
            if (loader.getDocumentId() == id) {
                loader.refresh();
            }
        }
    }

    /**
     * Upon connecting, populate the model with CustomDocumentLoader objects,
     * containing the correct {@link PersistentObject} object.
     *
     * @param container {@inheritDoc}
     * @throws org.aikodi.chameleon.workspace.ProjectException if there is
     * something wrong with the project this scanner is linked to
     * @see DocumentScannerContainer
     * @see PersistentObject
     * @see CustomDocumentLoader
     */
    @Override
    public void notifyContainerConnected(DocumentScannerContainer container) throws ProjectException {
        for (PersistentObject object : dao.getAllForProject(projectName)) {
            addToModel(object.getId());
        }
    }
}
