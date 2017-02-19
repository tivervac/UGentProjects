package be.ugent.vopro1.converter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the JsonConverter.
 *
 * @see JsonConverter
 */
public class JsonConverterImpl implements JsonConverter {

    private ObjectMapper objectMapper;
    private Map<String, Class> entityTypes;

    /**
     * Creates a new JsonConverter
     */
    public JsonConverterImpl() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JSR310Module());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        entityTypes = new HashMap<>();
    }

    @Override
    public void registerSerialize(Class c, JsonSerializer serializer) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(c, serializer);
        objectMapper.registerModule(module);
    }

    @Override
    public void registerDeserialize(Class c, JsonDeserializer deserializer) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(c, deserializer);
        objectMapper.registerModule(module);
    }

    @Override
    public void registerEntityType(Class<? extends Declaration> c) {
        if (c == null){
            throw new IllegalArgumentException();
        }

        entityTypes.put(c.toString(), c);
    }

    @Override
    public Class getType(String identifier){
        return entityTypes.get(identifier);
    }


    @Override
    public <C> C convert(Class<C> c, String blob) throws IOException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.readValue(blob, c);
    }

    @Override
    public String convertToString(Object o) throws JsonProcessingException {
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        return objectMapper.writeValueAsString(o);
    }

    @Override
    public <T> List<T> convertToList(String s, Class<T> typedef) throws IOException {
        List<T> result;
        TypeFactory t = TypeFactory.defaultInstance();
        result = objectMapper.readValue(s, t.constructCollectionType(ArrayList.class, typedef));
        return result;
    }

}
