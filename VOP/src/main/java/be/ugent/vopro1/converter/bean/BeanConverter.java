package be.ugent.vopro1.converter.bean;

/**
 * Converts an object to an output type
 * @param <O> Output type
 */
public interface BeanConverter<O> {

    /**
     * Converts the input
     *
     * @param input Input to convert
     * @return Converted bean
     */
    O convert(Object input);

}
