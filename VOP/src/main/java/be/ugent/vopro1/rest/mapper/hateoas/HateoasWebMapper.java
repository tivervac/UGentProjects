package be.ugent.vopro1.rest.mapper.hateoas;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.ResultType;
import be.ugent.vopro1.rest.mapper.AbstractWebMapper;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.presentationmodel.ApiPresentationModel;
import be.ugent.vopro1.rest.presentationmodel.Error;
import be.ugent.vopro1.rest.presentationmodel.PaginatedListPresentationModel;
import be.ugent.vopro1.rest.presentationmodel.PresentationModel;
import be.ugent.vopro1.util.LinkableList;
import be.ugent.vopro1.util.LocalConstants;
import be.ugent.vopro1.util.pagination.PaginatedArrayList;
import be.ugent.vopro1.util.pagination.PaginatedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Mapper used to generate a Spring ResponseEntity based on the the Result
 * created by the {@link be.ugent.vopro1.adapter.AdapterManager}.
 *
 * @see WebMapper
 * @see ResponseEntity
 * @see Result
 */
public class HateoasWebMapper extends AbstractWebMapper {

    private Map<Class<?>, Map<String, String>> generalLinksMap;
    private Map<Class<?>, Map<String, String>> specificLinksMap;

    @Override
    protected Object convertToPresentationModel(Object content, String requestPath, String requestQuery, Integer page) {
        LinkableList list = new LinkableList();

        if (content instanceof List<?>) {
            list.setContent(((List<?>) content)
                    .stream()
                    .map((a) -> this.convertEntityToPresentationModel(a, requestPath, false))
                    .collect(Collectors.toCollection(ArrayList::new)));

            List presentableContent = (List) list.getContent();
            if (!(presentableContent.isEmpty() || presentableContent.get(0) == null)) {
                list.setLinks(generalLinksMap.get(((List) list.getContent()).get(0).getClass()));
            }

            // Paginate the result if requested
            if (page != null && page >= 0) {
                PaginatedList paginatedList = new PaginatedArrayList(LocalConstants.PAGE);
                paginatedList.addAll((List<?>) list.getContent());
                paginatedList.page(page-1); // Paginated list is 0-indexed
                PaginatedListPresentationModel presModel = new PaginatedListPresentationModel();
                presModel.setEntity(paginatedList);
                presModel.setBaseUrl(requestPath);
                presModel.setQueryString(requestQuery);
                list.setContent(presModel);
            }

            return list;
        } else {
            list.setContent(convertEntityToPresentationModel(content, requestPath, true));

            return list;
        }
    }

    /**
     * Converts (part of the) content of a Result to its corresponding
     * PresentationModel. It is assured that the Object to convert is a single
     * Entity object.
     *
     * @param content (part of) content of the Result (single Entity object)
     * @return PresentationModel of the Result's content
     * @see PresentationModel
     * @see Result
     */
    private PresentationModel convertEntityToPresentationModel(Object content, String requestPath, boolean generalLinks) {
        try {
            PresentationModel model = (content != null) ? (PresentationModel) presentationModelMap.get(content.getClass()).newInstance()
                                                        : new ApiPresentationModel();
            model.setEntity(content);

            return addLinksToPresentationModel(model, requestPath, generalLinks);
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            logger.error("Encountered an unknown entity: {}", content.getClass(), e);
            return null;
        }
    }

    private PresentationModel addLinksToPresentationModel(PresentationModel model, String requestPath, boolean generalLinks) {
        if (generalLinks) {
            model.setLinks(generalLinksMap.get(model.getClass()));
        }
        if (!requestPath.isEmpty()) {
            for (String tag : specificLinksMap.get(model.getClass()).keySet()) {
                model.addLink(tag, fill(specificLinksMap.get(model.getClass()).get(tag), requestPath, model.getIdentifier()));
            }
        }

        for (Supplier<? extends PresentationModel> supplier : model.subPresentationModels()) {
            addLinksToPresentationModel(supplier.get(), requestPath, generalLinks);
        }

        for (Supplier<List<? extends PresentationModel>> supplier : model.subListPresentationModels()) {
            for (PresentationModel subModel : supplier.get()) {
                addLinksToPresentationModel(subModel, requestPath, generalLinks);
            }
        }

        return model;
    }

    private String fill(String url, String requestPath, String... parameters) {
        // Replace url with the requestPath minus its prepended /
        String filledUrl = url.replaceFirst("\\{url\\}", requestPath.substring(1,requestPath.length()));
        for (String parameter : parameters) {
            filledUrl = filledUrl.replaceFirst("\\{[^}]*\\}", parameter);
        }

        return filledUrl;
    }

    /**
     * Setter method to associate the correct specific links for entities
     * @param m New map to use
     */
    @Override
    public void setSpecificLinksMap(Map<Class<?>, Map<String, String>> m) {
        specificLinksMap = new HashMap<>(m);
    }

    /**
     * Setter method to associate the correct specific links for entities
     * @param m New map to use
     */
    @Override
    public void setGeneralLinksMap(Map<Class<?>, Map<String, String>> m) {
        generalLinksMap = new HashMap<>(m);
    }

}
