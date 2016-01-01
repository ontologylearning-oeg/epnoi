package org.epnoi.storage;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.storage.model.Source;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by cbadenes on 01/01/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Config.class)
@TestPropertySource(properties = {
        "epnoi.cassandra.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research",
        "epnoi.elasticsearch.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.elasticsearch.port = 5021",
        "epnoi.neo4j.contactpoints = zavijava.dia.fi.upm.es",
        "epnoi.neo4j.port = 5030"})
public class UDMTest {

    private static final Logger LOG = LoggerFactory.getLogger(UDMTest.class);

    @Autowired
    UDM udm;


    @Test
    public void saveSource(){

        Source source = new Source();
        source.setUri("http://epnoi.org/sources/0b3e80ae-d598-4dd4-8c54-38e2229f0bf8");
        source.setUrl("file://opt/epnoi/inbox/upm");
        source.setName("test-source");
        source.setProtocol("file");
        source.setCreationTime("20160101T22:02");
        source.setDescription("testing purposes");

        LOG.info("Saving source: " + source);
        udm.saveSource(source);
        LOG.info("source saved!");

        Source source2 = udm.readSource(source.getUri());
        Assert.assertEquals(source.getUri(),source2.getUri());
        Assert.assertEquals(source.getName(),source2.getName());

        LOG.info("Deleting source: " + source);
        udm.deleteSource(source.getUri());
        LOG.info("source deleted!");

        Source source3 = udm.readSource(source.getUri());
        Assert.assertNotEquals(source,source2);

    }

}
