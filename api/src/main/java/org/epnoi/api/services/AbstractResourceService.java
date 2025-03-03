package org.epnoi.api.services;

import com.google.common.base.Strings;
import org.epnoi.api.model.relations.RelationI;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.UDM;
import org.epnoi.model.utils.TimeUtils;
import org.epnoi.storage.generator.URIGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

/**
 * Created by cbadenes on 18/01/16.
 */

public abstract class AbstractResourceService<T extends Resource> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractResourceService.class);

    private final Resource.Type type;

    @Autowired
    protected UDM udm;

    @Autowired
    URIGenerator uriGenerator;

    public AbstractResourceService(Resource.Type type){
        this.type = type;
    }


    public T create(T resource) throws Exception {
        LOG.info("Trying to create: " + resource);
        udm.save(resource);
        return resource;
    }

    public T update(String id,T resource){
        String uri = uriGenerator.from(type,id);
        LOG.debug("updating by uri: " + uri);
        Optional<Resource> result = udm.read(type).byUri(uri);
        if (!result.isPresent()){
            throw new RuntimeException("Resource does not exist with uri: " + uri);
        }
        T original = (T) result.get();
        BeanUtils.copyProperties(resource,original);
        udm.save(original);
        return original;
    }


    public void remove(String id){
        String uri = uriGenerator.from(type,id);
        LOG.debug("removing by uri: " + uri);
        udm.delete(type).byUri(uri);
    }

    public void removeAll(){
        udm.delete(type).all();
    }

    public List<String> list(){
        return udm.find(type).all();
    }


    public T get(String id){
        String uri = uriGenerator.from(type,id);
        LOG.debug("getting by uri: " + uri);
        Optional<Resource> result = udm.read(type).byUri(uri);
        if (!result.isPresent())
            return null; //TODO Handle empty result
        return (T) result.get();
    }


}
