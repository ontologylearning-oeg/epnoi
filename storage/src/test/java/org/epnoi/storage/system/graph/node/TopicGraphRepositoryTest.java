package org.epnoi.storage.system.graph.node;

import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.epnoi.storage.system.graph.repository.nodes.ResourceGraphRepository;
import org.epnoi.storage.system.graph.repository.nodes.TopicGraphRepository;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TopicGraphRepositoryTest extends BaseGraphRepositoryTest<TopicNode> {

    private static final Logger LOG = LoggerFactory.getLogger(TopicGraphRepositoryTest.class);

    @Autowired
    TopicGraphRepository repository;

    @Override
    public ResourceGraphRepository<TopicNode> getRepository() {
        return repository;
    }

    @Override
    public TopicNode getEntity() {
        TopicNode node = new TopicNode();
        node.setUri("topics/72ce5395-6268-439a-947e-802229e7f022");
        node.setCreationTime("2015-12-21T16:18:59Z");
        return node;
    }


    @Test
    public void findByDomain(){

        String domainURI = "http://epnoi.org/domains/90e8b648-1b37-4756-9892-292560725a85";
        Iterable<TopicNode> nodes = repository.findByDomain(domainURI);
        LOG.info("Nodes: " + nodes);
    }

}
