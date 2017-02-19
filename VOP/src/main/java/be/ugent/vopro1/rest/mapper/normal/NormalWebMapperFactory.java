package be.ugent.vopro1.rest.mapper.normal;

import be.ugent.vopro1.rest.mapper.AbstractWebMapperFactory;
import be.ugent.vopro1.rest.mapper.WebMapper;

/**
 * WebMapperFactory for the {@link NormalWebMapper}
 */
public class NormalWebMapperFactory extends AbstractWebMapperFactory {

    private WebMapper instance;

    @Override
    public WebMapper getInstance() {
        if (instance == null) {
            instance = new NormalWebMapper();
            initialize();
        }

        return instance;
    }

    @Override
    public void setInstance(WebMapper mapper) {
        instance = mapper;
    }

}
