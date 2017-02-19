package be.ugent.vopro1.util.pagination;

import java.util.List;

/**
 * Provides an interface that all paginated lists should implement
 *
 * @param <T> Type of objects that the list will hold
 */
public interface PaginatedList<T> extends List<T> {

    /**
     * Returns the size of a single page
     * @return size of a single page
     */
    int getPageSize();

    /**
     * Returns <code>true</code> if the current page is the first possible page.
     *
     * @return <code>true</code> if the current page is the first possible page;
     *         <code>false</code> otherwise
     */
    boolean isFirstPage();

    /**
     * Returns <code>true</code> if the current page is an intermediate (not first, not last) page.
     *
     * @return <code>true</code> if the current page is an intermediate page.
     *         <code>false</code> otherwise
     */
    boolean isIntermediatePage();

    /**
     * Returns <code>true</code> if the current page is the last possible page.
     *
     * @return <code>true</code> if the current page is the last possible page;
     *         <code>false</code> otherwise
     */
    boolean isLastPage();

    /**
     * Returns <code>true</code> if the the pagination has a next page
     * <p>
     * Will return <code>true</code> if {@link #isLastPage()} returns <code>false</code>
     *
     * @return <code>true</code> if the pagination has a next page;
     *         <code>false</code> otherwise
     */
    boolean hasNextPage();

    /**
     * Returns <code>true</code> if the the pagination has a previous page
     * <p>
     * Will return <code>true</code> if {@link #isFirstPage()} returns <code>false</code>
     *
     * @return <code>true</code> if the pagination has a previous page;
     *         <code>false</code> otherwise
     */
    boolean hasPreviousPage();

    /**
     * Switches to the next page
     */
    void nextPage();

    /**
     * Switches to the previous page
     */
    void previousPage();

    /**
     * Returns the page that is currently selected
     *
     * @return the page that is currently selected
     */
    List<T> current();

    /**
     * Switches to a different page
     *
     * @param newPage the page to switch to
     */
    void page(int newPage);

    /**
     * Returns the number of the page that is currently selected
     *
     * @return the page that is currently selected
     */
    int page();

    /**
     * Returns the total number of data objects stored in the list
     *
     * @return the total number of data objects stored in the list
     */
    int size();

    /**
     * Returns the number of data objects stored in the current page
     *
     * @return the number of data objects stored in the current page
     */
    int count();
}
