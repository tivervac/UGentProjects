package be.ugent.vopro1.rest.mapper.normal;

import be.ugent.vopro1.rest.mapper.AbstractWebMapper;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.presentationmodel.PaginatedListPresentationModel;
import be.ugent.vopro1.rest.presentationmodel.PresentationModel;
import be.ugent.vopro1.util.LocalConstants;
import be.ugent.vopro1.util.pagination.PaginatedArrayList;
import be.ugent.vopro1.util.pagination.PaginatedList;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic {@link WebMapper} with no extra features
 */
public class NormalWebMapper extends AbstractWebMapper {

    protected Object convertToPresentationModel(Object content, String requestPath, String requestQuery, Integer page) {
        if (content instanceof List<?>) {
            List<?> list = ((List<?>) content)
                    .stream()
                    .map(this::convertEntityToPresentationModel)
                    .collect(Collectors.toList());

            // Paginate the result if requested
            if (page != null && page >= 0) {
                PaginatedList paginatedList = new PaginatedArrayList(LocalConstants.PAGE);
                paginatedList.addAll(list);
                paginatedList.page(page-1); // Paginated list is 0-indexed

                PaginatedListPresentationModel presModel = new PaginatedListPresentationModel();
                presModel.setEntity(paginatedList);
                presModel.setBaseUrl(requestPath);
                presModel.setQueryString(requestQuery);
                return presModel;
            }

            return list;
        } else {
            return convertEntityToPresentationModel(content);
        }
    }

    private Object convertEntityToPresentationModel(Object content) {
        try {
            PresentationModel model = (PresentationModel) presentationModelMap.get(content.getClass()).newInstance();
            model.setEntity(content);
            return model;
        } catch (InstantiationException | IllegalAccessException e) {
            logger.error("Encountered an unknown entity: {}", content.getClass(), e);
            return null;
        }
    }

}
