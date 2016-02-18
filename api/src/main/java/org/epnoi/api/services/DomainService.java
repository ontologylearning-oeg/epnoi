package org.epnoi.api.services;

import org.epnoi.model.domain.resources.Domain;
import org.epnoi.model.domain.resources.Resource;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 18/01/16.
 */
@Component
public class DomainService extends AbstractCRUDService<Domain> {

    public DomainService() {
        super(Resource.Type.DOMAIN);
    }
}
