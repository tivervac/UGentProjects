package be.ugent.vopro1.converter;

import be.ugent.vopro1.converter.bean.BeanConverter;

import java.util.List;

/**
 * Provides a facade to the "converter" module
 */
public interface ConverterFacade {

    /**
     * Converts the input to the requested output
     *
     * @param input Input to convert
     * @param <I> Type of the input
     * @param <O> Type of the output
     * @return Converted output
     */
    <I, O> O convert(I input);

    /**
     * Converts an interable of inputs to an iterable of outputs
     *
     * @param inputs Inputs to convert
     * @param <I> Type of the inputs
     * @param <O> Type of the outputs
     * @return Converted outputs
     */
    <I, O> List<O> convert(List<I> inputs);

    /**
     * Sets the {@link ConverterProvider} used by the facade
     *
     * @param provider Provider to set
     * @see ConverterProvider
     */
    void setProvider(ConverterProvider provider);

    /**
     * Retrieves the {@link ConverterProvider} that is currently in use by the facade
     *
     * @return Current ConverterProvider
     * @see ConverterProvider
     */
    ConverterProvider getProvider();

    /**
     * Sets a {@link BeanConverter} for a class
     *
     * @param clazz Class that can be converted
     * @param converter Converter to use
     * @see BeanConverter
     */
    void setConverter(Class clazz, BeanConverter converter);

    /**
     * Retrieves the {@link BeanConverter} for a class
     *
     * @param clazz Class you want to convert
     * @return Converter responsible for given class
     * @see BeanConverter
     */
    BeanConverter getConverter(Class clazz);

}
