package be.ugent.vopro1.converter;

import be.ugent.vopro1.converter.bean.BeanConverter;

/**
 * Provides {@link BeanConverter}s to the {@link ConverterFacade}
 */
public interface ConverterProvider {

    /**
     * Registers a {@link BeanConverter} for given class
     *
     * @param clazz Class that the converter is responsible for
     * @param converter Converter that can convert this class
     * @param <O> Type of the output
     */
    <O> void register(Class clazz, BeanConverter<O> converter);

    /**
     * Retrieves the {@link BeanConverter} responsible for given class
     *
     * @param clazz Class you wish to retrieve converter for
     * @return Requested converter
     */
    BeanConverter get(Class clazz);

}
