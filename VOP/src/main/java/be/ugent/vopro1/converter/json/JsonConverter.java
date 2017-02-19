package be.ugent.vopro1.converter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.io.IOException;
import java.util.List;

/**
 * Provides converters from and to JSON blobs
 */
public interface JsonConverter {

    /**
     * Converts a blob to the specific class and returns an instance of it.
     *
     * @param c Class to convertToString to
     * @param blob Blob to convertToString
     * @param <C> Class to convert blob into
     * @return Converted instance
     * @throws IOException if the mapper is unable to read the blob
     */
    <C> C convert(Class<C> c, String blob) throws IOException;

    /**
     * Convert an Object into a blob using Jackson.
     *
     * @param o the Object from which we need the String representation
     * @return the blob created by Jackson from o
     * @throws JsonProcessingException if the mapper if unable to process the given object
     */
    String convertToString(Object o) throws JsonProcessingException;

    /**
     * Converts a string to an ArrayList of the given type
     * @param s string to convert
     * @param typedef class object of the desired type
     * @param <T> the desired type
     * @return deserialized list of objects
     * @throws IOException if the mapper is unable to read the blob
     */
    <T> List<T> convertToList(String s, Class<T> typedef) throws IOException;

    /**
     * Register a Serializer for a certain class
     * @param c the class for which the serializer is being registered
     * @param serializer the serializer reference
     */
    void registerSerialize(Class c, JsonSerializer serializer);

    /**
     * Register a Deserializer for a certain class
     * @param c the class for which the deserialize is being registered
     * @param deserializer the deserializer reference
     */
    void registerDeserialize(Class c, JsonDeserializer deserializer);

    /**
     * Helper method to reduce level of reflection
     * Registers a class type inside the converter
     * @param c Entity type to register
     */
    void registerEntityType(Class<? extends Declaration> c);

    /**
     * Returns the entity type for given identifier
     *
     * @param typeString Type string to return entityType for
     * @return requested entityType class
     */
    Class getType(String typeString);

}
