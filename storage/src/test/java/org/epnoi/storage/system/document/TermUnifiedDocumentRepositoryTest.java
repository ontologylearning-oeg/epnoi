package org.epnoi.storage.system.document;

import org.epnoi.storage.system.document.domain.TermDocument;
import org.epnoi.storage.system.document.domain.WordDocument;
import org.epnoi.storage.system.document.repository.BaseDocumentRepository;
import org.epnoi.storage.system.document.repository.TermDocumentRepository;
import org.epnoi.storage.system.document.repository.WordDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TermUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<TermDocument> {

    @Autowired
    TermDocumentRepository repository;

    @Override
    public BaseDocumentRepository<TermDocument> getRepository() {
        return repository;
    }

    @Override
    public TermDocument getEntity() {
        TermDocument document = new TermDocument();
        document.setUri("words/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setContent("molecular");
        return document;
    }
}
