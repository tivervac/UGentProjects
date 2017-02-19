package be.ugent.vopro1.converter.bean;

import be.ugent.vopro1.converter.ConverterProvider;

/**
 * Abstract BeanConverter implementation with an output type argument
 *
 * @param <I> Input type of the converter
 * @param <O> Output type of the converter
 */
public abstract class AbstractBeanConverter<I, O> implements BeanConverter<O> {

    /**
     * Creates a new AbstractBeanConverter for given class and registers it in the provider
     *
     * @param provider Provider to register converter in
     * @param clazz Class that this bean can convert
     */
    public AbstractBeanConverter(ConverterProvider provider, Class<I> clazz) {
        provider.register(clazz, this);
    }

    /**
     * {@inheritDoc}
     *
     * @param input {@inheritDoc}
     * @return {@inheritDoc}
     * @see BeanConverter
     */
    public O convert(Object input) {
        return convertBean((I) input);
    }

    /**
     * Converts a bean with given input type to the output type
     *
     * @param input Input to convert
     * @return converted bean
     */
    public abstract O convertBean(I input);
}
