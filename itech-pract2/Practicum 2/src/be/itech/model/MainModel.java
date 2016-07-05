package be.itech.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

/**
 * This is a class which implements a special variation of the singleton model.
 * It holds a map which keeps a singleton for every generic datatype.
 *
 * @author Titouan Vervack & Caroline De Brouwer
 * @param <T> The type
 */
public class MainModel<T> extends Observable {

    private List<T> data;
    private static final Map<Class, Object> map = new HashMap<Class, Object>();

    public static <T> MainModel<T> getInstance(Class<T> classOf) {
        synchronized (map) {
            if (!map.containsKey(classOf)) {
                map.put(classOf, new MainModel<T>());
            }
            return (MainModel<T>) map.get(classOf);
        }
    }

    public static <T> MainModel<T> getInstance(Object o) {
        synchronized (map) {
            if (!map.containsKey(o.getClass())) {
                map.put(o.getClass(), new MainModel<T>());
            }
            return (MainModel<T>) map.get(o.getClass());
        }
    }

    private void notifyAllObservers() {
        setChanged();
        notifyObservers();
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyAllObservers();
    }

    public List<T> getData() {
        if (data == null) {
            data = new ArrayList<T>();
        }
        return data;
    }

    public void removeElement(T element) {
        data.remove(element);
        notifyAllObservers();
    }

    public void addElement(T element) {
        if (data == null) {
            data = new ArrayList<T>();
        }
        data.add(element);
        setChanged();
        notifyObservers(element);
    }

    public void editElement(T element) {
        data.set(data.indexOf(element), element);
        notifyAllObservers();
    }

    public T getElement(T element) {
        return data.get(data.indexOf(element));
    }
}
