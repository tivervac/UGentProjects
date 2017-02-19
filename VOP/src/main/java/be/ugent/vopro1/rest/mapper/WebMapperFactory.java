package be.ugent.vopro1.rest.mapper;

/**
 * Interface that all WebMapperFactories should implement
 */
public interface WebMapperFactory {

    /**
     * Retrieves the WebMapper instance that this factory is responsible for
     * @return current instance
     */
    WebMapper getInstance();

    /**
     * Sets a new instance that will be used in future {@link #getInstance()} calls
     *
     * @param mapper instance to set
     */
    void setInstance(WebMapper mapper);

}
