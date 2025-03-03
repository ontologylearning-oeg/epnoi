package org.epnoi.storage.system.column;

import org.epnoi.storage.system.column.domain.AnalysisColumn;
import org.epnoi.storage.system.column.repository.AnalysisColumnRepository;
import org.epnoi.storage.system.column.repository.BaseColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by cbadenes on 22/12/15.
 */
public class AnalysisUnifiedColumnRepositoryTest extends BaseColumnRepositoryTest<AnalysisColumn> {

    @Autowired
    AnalysisColumnRepository repository;

    @Override
    public BaseColumnRepository<AnalysisColumn> getRepository() {
        return repository;
    }

    @Override
    public AnalysisColumn getEntity() {

        AnalysisColumn column = new AnalysisColumn();
        column.setUri("relations/72ce5395-6268-439a-947e-802229e7f022");
        column.setCreationTime("2015-12-21T16:18:59Z");
        column.setType("topicModel");
        column.setConfiguration("alpha=16.1, beta=1.1, topics=8");
        column.setDomain("domains/72ce5395-6268-439a-947e-802229e7f022");
        return column;
    }
}
