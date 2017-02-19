package be.ugent.vopro1.converter;

import be.ugent.vopro1.converter.bean.BeanConverter;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the {@link ConverterProvider}
 */
public class DefaultConverterProvider implements ConverterProvider {

    private Map<Class, BeanConverter> converters;

    /**
     * Creates a new DefaultConverterProvider
     */
    public DefaultConverterProvider() {
        this.converters = new HashMap<>();
    }

    @Override
    public void register(Class clazz, BeanConverter converter) {
        converters.put(clazz, converter);
    }

    @Override
    public BeanConverter get(Class clazz) {
        if (converters.containsKey(clazz)) {
            return converters.get(clazz);
        } else {
            return converters.get(Declaration.class);
        }
    }

}
