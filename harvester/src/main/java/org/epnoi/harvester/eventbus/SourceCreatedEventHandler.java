package org.epnoi.harvester.eventbus;

import org.epnoi.harvester.services.SourceService;
import org.epnoi.model.Event;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.model.domain.resources.Source;
import org.epnoi.model.modules.BindingKey;
import org.epnoi.model.modules.EventBus;
import org.epnoi.model.modules.EventBusSubscriber;
import org.epnoi.model.modules.RoutingKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by cbadenes on 01/12/15.
 */
@Component
public class SourceCreatedEventHandler implements EventBusSubscriber {

    private static final Logger LOG = LoggerFactory.getLogger(SourceCreatedEventHandler.class);

    @Autowired
    SourceService sourceService;

    @Autowired
    protected EventBus eventBus;


    @PostConstruct
    public void init(){
        RoutingKey routingKey = RoutingKey.of(Resource.Type.SOURCE, Resource.State.CREATED);
        LOG.info("Trying to register as subscriber of '" + routingKey + "' events ..");
        eventBus.subscribe(this, BindingKey.of(routingKey, "harvester-source"));
        LOG.info("registered successfully");
    }


    @Override
    public void handle(Event event) {
        LOG.info("New Source event received: " + event);
        try{
            sourceService.create(event.to(Source.class));
        } catch (RuntimeException e){
            // TODO Notify to event-bus when source has not been added
            LOG.warn(e.getMessage());
        }catch (Exception e){
            // TODO Notify to event-bus when source has not been added
            LOG.error("Error adding new source: " + event, e);
        }
    }
}
