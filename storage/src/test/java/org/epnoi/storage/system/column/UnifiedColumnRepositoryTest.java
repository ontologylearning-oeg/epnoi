package org.epnoi.storage.system.column;

import es.cbadenes.lab.test.IntegrationTest;
import org.epnoi.model.domain.resources.*;
import org.epnoi.storage.system.column.repository.UnifiedColumnRepository;
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

import java.util.Optional;

/**
 * Created by cbadenes on 03/02/16.
 */
@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ColumnConfig.class)
@TestPropertySource(properties = {
        "epnoi.cassandra.contactpoints = drinventor.dia.fi.upm.es",
        "epnoi.cassandra.port = 5011",
        "epnoi.cassandra.keyspace = research" })
public class UnifiedColumnRepositoryTest {

    private static final Logger LOG = LoggerFactory.getLogger(UnifiedColumnRepositoryTest.class);

    @Autowired
    UnifiedColumnRepository unifiedColumnRepository;


    @Test
    public void source(){

        Source source = Resource.newSource();
        source.setUri("sources/01");
        source.setName("test01");
        source.setDescription("testing purposes");
        test(source, Resource.Type.SOURCE);
    }

    @Test
    public void domain(){

        Domain domain = Resource.newDomain();
        domain.setUri("domains/01");
        domain.setName("test01");
        domain.setDescription("testing purposes");
        test(domain,Resource.Type.DOMAIN);
    }

    @Test
    public void document() {

        Document document = Resource.newDocument();
        document.setUri("documents/01");
        document.setAuthoredBy("me");
        document.setAuthoredOn("20151210");
        document.setDescription("testing purposes");
        test(document, Resource.Type.DOCUMENT);
    }

    @Test
    public void item() {

        Item resource = Resource.newItem();
        resource.setUri("items/01");
        resource.setAuthoredBy("me");
        resource.setAuthoredOn("20151210");
        resource.setDescription("testing purposes");
        test(resource, Resource.Type.ITEM);
    }

    @Test
    public void part() {

        Part resource = Resource.newPart();
        resource.setUri("items/01");
        resource.setSense("nosense");
        resource.setContent("sampling");
        resource.setTokens("sample");
        test(resource, Resource.Type.PART);
    }

    @Test
    public void topic() {

        Topic resource = Resource.newTopic();
        resource.setUri("items/01");
        resource.setAnalysis("analyses/01");
        resource.setContent("sampling");
        test(resource, Resource.Type.TOPIC);
    }

    @Test
    public void word() {

        Word resource = Resource.newWord();
        resource.setUri("items/01");
        resource.setContent("house");
        test(resource, Resource.Type.WORD);
    }


    private void test(Resource resource, Resource.Type type){

        LOG.info("####################### " + type.name());

        unifiedColumnRepository.deleteAll(type);

        Assert.assertFalse(unifiedColumnRepository.exists(type,resource.getUri()));

        unifiedColumnRepository.save(resource);

        Assert.assertTrue(unifiedColumnRepository.exists(type,resource.getUri()));
        Optional<Resource> result = unifiedColumnRepository.read(type,resource.getUri());
        Assert.assertTrue(result.isPresent());

        Resource resource2 = result.get();

        Assert.assertEquals(resource,result.get());

        unifiedColumnRepository.delete(type,resource.getUri());

        Assert.assertFalse(unifiedColumnRepository.exists(type,resource.getUri()));
    }


    @Test
    public void findBy(){

        unifiedColumnRepository.deleteAll(Resource.Type.DOCUMENT);

        Document document = new Document();
        document.setUri("documents/01");
        document.setTitle("This is a title");
        unifiedColumnRepository.save(document);

        Iterable<Resource> documents = unifiedColumnRepository.findBy(Resource.Type.DOCUMENT, "title", document.getTitle());
        System.out.println("Documents: " + documents);

    }
}
