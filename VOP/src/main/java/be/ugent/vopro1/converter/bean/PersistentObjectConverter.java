package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.util.RuntimeIOException;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts {@link PersistentObject} into {@link Declaration}
 */
public class PersistentObjectConverter extends AbstractBeanConverter<PersistentObject, Declaration> {

    private JsonConverter jsonConverter;
    private Map<String, Class<? extends Declaration>> types;

    public void setTypes(Map<String, Class<? extends Declaration>> map){
        this.types = new HashMap<>(map);
    }

    /**
     * Creates a new PersistentObjectConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public PersistentObjectConverter(ConverterProvider provider) {
        super(provider, PersistentObject.class);
        this.jsonConverter = JsonConverterFactory.getInstance();
    }

    @Override
    public Declaration convertBean(PersistentObject input) {
        Declaration converted;

        String typeString = input.getType();
        Class<? extends Declaration> type = types.get(typeString);
        if (type == null) {
            throw new IllegalArgumentException("The type name in the given object is not supported");
        }

        try {
            converted = jsonConverter.convert(type, input.getBlob());
        } catch (IOException e) {
            throw new RuntimeIOException(e);
        }

        return converted;
    }
}
