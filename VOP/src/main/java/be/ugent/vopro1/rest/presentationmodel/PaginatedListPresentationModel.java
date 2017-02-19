package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.util.LocalConstants;
import be.ugent.vopro1.util.pagination.PaginatedList;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to present the data of a {@link PaginatedList} to the user.
 *
 * @see PresentationModel
 * @see PaginatedList
 */
public class PaginatedListPresentationModel extends PresentationModel {

    private static final String PAGE_ARGUMENT = "?page=";
    private static final String NEXT_LINK_TITLE = "next";
    private static final String PREVIOUS_LINK_TITLE = "previous";
    private PaginatedList<?> list;
    private String baseUrl;
    private String queryString;

    @Override
    public void setEntity(Object entity) {
        this.list = (PaginatedList<?>) entity;
    }

    /**
     * Sets the base url used for the retrieval
     *
     * @param baseUrl Base URL to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = LocalConstants.HOME + baseUrl;
        addLink(NEXT_LINK_TITLE, getNext());
        addLink(PREVIOUS_LINK_TITLE, getPrevious());
    }

    /**
     * Sets the query string used for retrieval
     *
     * @param queryString Query string to set
     */
    public void setQueryString(String queryString) {
        this.queryString = queryString;
        addLink(NEXT_LINK_TITLE, getNext());
        addLink(PREVIOUS_LINK_TITLE, getPrevious());
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    /**
     * Returns the current page of the PaginatedList
     *
     * @return current page of the PaginatedList
     */
    public List<?> getPage() {
        return list.current();
    }

    /**
     * Returns a map of metadata about the pagination
     *
     * @return metadata about the pagination state
     */
    public Map<String, Object> getMetadata() {
        Map<String, Object> metadata = new ConcurrentHashMap<>();
        metadata.put("hasNextPage", list.hasNextPage());
        metadata.put("hasPreviousPage", list.hasPreviousPage());
        metadata.put("firstPage", list.isFirstPage());
        metadata.put("lastPage", list.isLastPage());
        metadata.put("pageNumber", list.page() + 1);                // Paginated list is zero indexed
        metadata.put("pageSize", list.getPageSize());
        metadata.put("size", list.size());
        metadata.put("length", list.count());
        return metadata;
    }

    private String additionalParameters() {
        if (queryString == null) {
            return "";
        }

        // Remove page argument if it already exists
        queryString = queryString.replaceAll("&*page=[0-9]+", "");

        if (queryString == null || queryString.length() < 3) {
            return "";
        } else {
            // Make sure there are no double ampersands (just for URL nicety)
            return ("&" + queryString).replaceAll("&&", "&");
        }
    }

    private String getNext() {
        if (list.hasNextPage()) {
            return baseUrl + PAGE_ARGUMENT + ((list.page() + 1) + 1) + additionalParameters();    // Paginated list is zero indexed
        } else {
            return null; // No next page;
        }
    }

    private String getPrevious() {
        if (list.hasPreviousPage()) {
            return baseUrl + PAGE_ARGUMENT + ((list.page() + 1) - 1) + additionalParameters();    // Paginated list is zero indexed
        } else {
            return null; // No previous page;
        }
    }
}
