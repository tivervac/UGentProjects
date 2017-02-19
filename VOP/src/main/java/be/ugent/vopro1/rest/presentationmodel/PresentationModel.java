package be.ugent.vopro1.rest.presentationmodel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Interface used to present the Entity data to the user. For each Entity that
 * must be presented to the user, a corresponding PresentationModel will be
 * created, containing a reference to this Entity. For every attribute that must
 * be converted to JSON by Spring, an appropriate getter will be implemented.
 */
public abstract class PresentationModel {

    // Showing null when there are no links is useless
    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    protected Map<String, String> links;

    /**
     * Sets the reference of this PresentationModel to an Entity object, so that
     * all requested information for this PresentationModel can be delegated to
     * this Entity object.
     *
     * @param entity Entity object that must be represented by the
     * PresentationModel
     */
    public abstract void setEntity(Object entity);

    /**
     * Sets the links map directly
     *
     * @param links Links map to set
     */
    public void setLinks(Map<String, String> links) {
        this.links = new HashMap<>(links);
    }

    /**
     * Retrieves all links for this presentation model
     *
     * @return map of tags to links
     */
    public Map<String, String> getLinks() {
        return links;
    }

    /**
     * Adds a link for this presentation model
     * @param tag Tag to use
     * @param link Link to use
     */
    public void addLink(String tag, String link) {
        if (links == null) {
            links = new HashMap<>();
        }
        links.put(tag, link);
    }

    /**
     * Returns a list of interior presentation models that also require extra attention
     * For example, the {@link be.ugent.vopro1.rest.mapper.hateoas.HateoasWebMapper} uses it
     * to assign links to subModels
     *
     * @return Suppliers for the basic subPresentationModels
     */
    @JsonIgnore
    public List<Supplier<? extends PresentationModel>> subPresentationModels() {
        return new ArrayList<>();
    }

    /**
     * Returns a list of interior presentation models that also require extra attention
     * For example, the {@link be.ugent.vopro1.rest.mapper.hateoas.HateoasWebMapper} uses it
     * to assign links to subModels
     *
     * @return Suppliers for lists of subPresentationModels
     */
    @JsonIgnore
    public List<Supplier<List<? extends PresentationModel>>> subListPresentationModels() {
        return new ArrayList<>();
    }

    /**
     * Retrieve the identifier of the presentation model
     * @return Identifier
     */
    @JsonIgnore
    public abstract String getIdentifier();
}
