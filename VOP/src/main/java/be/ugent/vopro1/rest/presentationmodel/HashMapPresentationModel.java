package be.ugent.vopro1.rest.presentationmodel;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;

/**
 * PresentationModel for HashMap results. Jackson will automatically
 * serialize all the keys and their values to:
 * <code>"key": {
 *     "value"
 * }</code>
 *
 * @see HashMap
 */
public class HashMapPresentationModel extends PresentationModel {

    private HashMap<?, ?> map;

    @JsonValue
    public HashMap<?, ?> getMap() {
        return map;
    }

    @Override
    public void setEntity(Object entity) {
        map = (HashMap<?, ?>) entity;
    }

    @Override
    public String getIdentifier() {
        return null;
    }
}
