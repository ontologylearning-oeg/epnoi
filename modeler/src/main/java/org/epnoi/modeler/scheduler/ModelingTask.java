package org.epnoi.modeler.scheduler;

import org.epnoi.model.domain.Resource;
import org.epnoi.modeler.helper.ModelingHelper;
import org.epnoi.modeler.models.topic.TopicModeler;
import org.epnoi.modeler.models.word.WordEmbeddingModeler;
import org.epnoi.model.domain.Analysis;
import org.epnoi.model.domain.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by cbadenes on 13/01/16.
 */
public class ModelingTask implements Runnable{

    private static final Logger LOG = LoggerFactory.getLogger(ModelingTask.class);

    protected final Domain domain;
    protected final ModelingHelper helper;

    public ModelingTask(Domain domain, ModelingHelper modelingHelper){
        this.domain = domain;
        this.helper = modelingHelper;
    }

    protected Analysis newAnalysis(String type, String configuration, String description){

        Analysis analysis = new Analysis();
        analysis.setUri(helper.getUriGenerator().newFor(Resource.Type.ANALYSIS));
        analysis.setCreationTime(helper.getTimeGenerator().asISO());
        analysis.setDomain(domain.getUri());
        analysis.setType(type);
        analysis.setDescription(description);
        analysis.setConfiguration(configuration);
        return analysis;
    }

    @Override
    public void run() {
        helper.getModelBuilder().execute(new TopicModeler(domain,helper));
        helper.getModelBuilder().execute(new WordEmbeddingModeler(domain,helper));
    }
}
