package org.epnoi.storage.system.document;

import org.epnoi.storage.system.document.domain.ItemDocument;
import org.epnoi.storage.system.document.repository.BaseDocumentRepository;
import org.epnoi.storage.system.document.repository.ItemDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class ItemUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<ItemDocument> {

    @Autowired
    ItemDocumentRepository repository;

    @Override
    public BaseDocumentRepository<ItemDocument> getRepository() {
        return repository;
    }

    @Override
    public ItemDocument getEntity() {
        ItemDocument document = new ItemDocument();
        document.setUri("items/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setFormat("pdf");
        document.setLanguage("en");
        document.setTitle("This is an example");
        document.setSubject("semantic web, e-science");
        document.setDescription("for testing purposes");
        document.setUrl("file:://opt/drinventor/example.pdf");
        document.setContent("Miniopterus aelleni is a bat in the genus Miniopterus found in the Comoro Islands and Madagascar. It is a small, brown bat, with a forearm length of 35 to 41 mm (1.4 to 1.6 in). The long tragus (a projection in the outer ear) has a broad base and a blunt or rounded tip. The uropatagium (tail membrane) is sparsely haired. The palate is flat and there are distinct diastemata (gaps) between the upper canines and premolars. Populations of this species were previously included in Miniopterus manavi, but recent molecular studies revealed that M");
        return document;
    }
}
