package org.epnoi.storage.system.document;

import org.epnoi.storage.system.document.domain.TopicDocument;
import org.epnoi.storage.system.document.repository.BaseDocumentRepository;
import org.epnoi.storage.system.document.repository.TopicDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class TopicUnifiedDocumentRepositoryTest extends BaseDocumentRepositoryTest<TopicDocument> {

    @Autowired
    TopicDocumentRepository repository;

    @Override
    public BaseDocumentRepository<TopicDocument> getRepository() {
        return repository;
    }

    @Override
    public TopicDocument getEntity() {
        TopicDocument document = new TopicDocument();
        document.setUri("topics/72ce5395-6268-439a-947e-802229e7f022");
        document.setCreationTime("2015-12-21T16:18:59Z");
        document.setContent("molecular color graphic rendering");
        document.setAnalysis("analysis/72ce5395-6268-439a-947e-802229e7f022");
        return document;
    }
}
