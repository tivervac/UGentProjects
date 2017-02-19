package be.ugent.vopro1.converter;

import be.ugent.vopro1.converter.bean.BeanConverter;
import be.ugent.vopro1.util.ApplicationContextProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Default implementaton of a {@link ConverterFacade}
 */
public class DefaultConverterFacade implements ConverterFacade {

    private ConverterProvider provider;

    /**
     * Creates a new DefaultConverterFacade and retrieves the
     * provider from the "application-context.xml" file
     */
    public DefaultConverterFacade() {
        this.provider = ApplicationContextProvider.getApplicationContext().getBean("converterProvider", ConverterProvider.class);
    }

    @Override
    public void setProvider(ConverterProvider provider) {
        this.provider = provider;
    }

    @Override
    public ConverterProvider getProvider() {
        return provider;
    }

    @Override
    public void setConverter(Class clazz, BeanConverter converter) {
        provider.register(clazz, converter);
    }

    @Override
    public BeanConverter getConverter(Class clazz) {
        return provider.get(clazz);
    }

    @Override
    public <I, O> O convert(I input) {
        return (O) getConverter(input.getClass()).convert(input);
    }

    @Override
    public <I, O> List<O> convert(List<I> inputs) {
        List<O> outputs = new ArrayList<>();
        for (I input : inputs) {
            outputs.add(convert(input));
        }
        return outputs;
    }

}
