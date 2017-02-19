package be.ugent.vopro1.util.pagination;

import java.util.*;

/**
 * Provides an {@link ArrayList} implementation of the {@link PaginatedList} interface
 * <p>
 * This implementation is loosely based on the PaginatedArrayList demo made by Clinton Begin in 2004, released
 * under the Apache 2.0 license.
 * <a href="http://www.java2s.com/Code/Java/Collections-Data-Structure/ImplementationofPaginatedListbackedbyanArrayList.htm">
 *     Original implementation
 * </a>
 *
 *
 * @param <T> Type of objects that the list will hold
 */
public class PaginatedArrayList<T> implements PaginatedList<T> {

    private static final List EMPTY_LIST = new ArrayList<>();
    private List<T> list;
    private List<T> page;

    private int pageSize;
    private int index;

    /**
     * Constructs a PaginatedArrayList with the page size
     *
     * @param pageSize size of a page
     */
    public PaginatedArrayList(int pageSize) {
        this.pageSize = pageSize;
        this.index = 0;
        this.list = new ArrayList<>();
        repaginate();
    }

    /**
     * Constructs a PaginatedArrayList with the initial capacity and page size
     *
     * @param initialCapacity initial capacity of the backing list
     * @param pageSize size of a page
     */
    public PaginatedArrayList(int initialCapacity, int pageSize) {
        this.pageSize = pageSize;
        this.index = 0;
        this.list = new ArrayList<>(initialCapacity);
        repaginate();
    }

    /**
     * Constructs a PaginatedArrayList with the initial collection and page size
     *
     * @param collection initial collection to use
     * @param pageSize size of a page
     */
    public PaginatedArrayList(Collection<T> collection, int pageSize) {
        this.pageSize = pageSize;
        this.index = 0;
        this.list = new ArrayList<>(collection);
        repaginate();
    }

    /**
     * Repaginates the backing list when the index or backing list have changed
     */
    private void repaginate() {
        if (list.isEmpty()) {
            page = EMPTY_LIST;
        } else {
            int start = index * pageSize;
            int end = start + pageSize - 1;

            if (end >= list.size()) {
                // Reset the end if it is too large
                end = list.size() - 1;
            }

            if (start >= list.size()) {
                // Reset the start if it is too large
                index = 0;
                repaginate();
            } else if (start < 0) {
                // Reset the start if it is too small
                index = list.size() / pageSize;
                if (list.size() % pageSize == 0) {
                    index--;
                }
                repaginate();
            } else {
                // Paginate the backing list based on start and end
                page = list.subList(start, end + 1);
            }
        }
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public boolean isFirstPage() {
        return index == 0;
    }

    @Override
    public boolean isIntermediatePage() {
        return !(isFirstPage() || isLastPage());
    }

    @Override
    public boolean isLastPage() {
        return list.size() - ((index + 1) * pageSize) < 1;
    }

    @Override
    public boolean hasNextPage() {
        return !isLastPage();
    }

    @Override
    public boolean hasPreviousPage() {
        return !isFirstPage();
    }

    @Override
    public void nextPage() {
        if (hasNextPage()) {
            index++;
            repaginate();
        }
    }

    @Override
    public void previousPage() {
        if (hasPreviousPage()) {
            index--;
            repaginate();
        }
    }

    @Override
    public List<T> current() {
        return page;
    }

    @Override
    public void page(int newPage) {
        index = newPage;
        repaginate();
    }

    @Override
    public int page() {
        return index;
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public int count() {
        return page.size();
    }

    @Override
    public boolean isEmpty() {
        return page.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return page.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return page.iterator();
    }

    @Override
    public Object[] toArray() {
        return page.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return page.toArray(a);
    }

    @Override
    public boolean add(T t) {
        boolean added = list.add(t);
        repaginate();
        return added;
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = list.remove(o);
        repaginate();
        return removed;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return page.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean added = list.addAll(c);
        repaginate();
        return added;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean added = list.addAll(index, c);
        repaginate();
        return added;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean removed = list.removeAll(c);
        repaginate();
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean retained = list.retainAll(c);
        repaginate();
        return retained;
    }

    @Override
    public void clear() {
        list.clear();
        repaginate();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PaginatedArrayList<?> that = (PaginatedArrayList<?>) o;

        if (getPageSize() != that.getPageSize()) {
            return false;
        }
        if (index != that.index) {
            return false;
        }
        if (list != null ? !list.equals(that.list) : that.list != null) {
            return false;
        }
        return !(page != null ? !page.equals(that.page) : that.page != null);
    }

    @Override
    public int hashCode() {
        int zero = 0;
        int modifier = 31;
        int result = list != null ? list.hashCode() : zero;
        result = modifier * result + (page != null ? page.hashCode() : zero);
        result = modifier * result + getPageSize();
        result = modifier * result + index;
        return result;
    }

    @Override
    public T get(int index) {
        return page.get(index);
    }

    @Override
    public T set(int index, T element) {
        T elem = list.set(index, element);
        repaginate();
        return elem;
    }

    @Override
    public void add(int index, T element) {
        list.add(index, element);
        repaginate();
    }

    @Override
    public T remove(int index) {
        T elem = list.remove(index);
        repaginate();
        return elem;
    }

    @Override
    public int indexOf(Object o) {
        return page.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return page.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return page.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return page.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return page.subList(fromIndex, toIndex);
    }
}
