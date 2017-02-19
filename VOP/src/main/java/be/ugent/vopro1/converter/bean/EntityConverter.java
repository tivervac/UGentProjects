package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.converter.ConverterProvider;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.util.RuntimeIOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts {@link Declaration} into {@link PersistentObject}
 */
public class EntityConverter extends AbstractBeanConverter<Declaration, PersistentObject> {

    private JsonConverter jsonConverter;
    private Map<Class<? extends Declaration>, String> types;

    public void setTypes(Map<Class<? extends Declaration>, String> map){
        this.types = new HashMap<>(map);
    }

    /**
     * Creates a new EntityConverter and registers it in the provider
     *
     * @param provider Provider to register in
     * @see ConverterProvider#register(Class, BeanConverter)
     */
    public EntityConverter(ConverterProvider provider) {
        super(provider, Declaration.class);
        jsonConverter = JsonConverterFactory.getInstance();
    }

    @Override
    public PersistentObject convertBean(Declaration input) {
        try {
            String s = jsonConverter.convertToString(input);
            return new PersistentObject(-1, input.name(), types.get(input.getClass()), s);
        } catch (JsonProcessingException e) {
            throw new RuntimeIOException(e);
        }
    }
}
