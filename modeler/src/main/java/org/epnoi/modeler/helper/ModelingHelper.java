package org.epnoi.modeler.helper;

import lombok.Data;
import org.epnoi.modeler.builder.*;
import org.epnoi.model.utils.TimeUtils;
import org.epnoi.storage.UDM;
import org.epnoi.storage.generator.URIGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by cbadenes on 12/01/16.
 */
@Data
@Component
public class ModelingHelper {

    @Autowired
    SparkHelper sparkHelper;

    @Autowired
    URIGenerator uriGenerator;

    @Autowired
    AuthorBuilder authorBuilder;

    @Autowired
    ModelBuilder modelBuilder;

    @Autowired
    TopicModelBuilder topicModelBuilder;

    @Autowired
    RegularResourceBuilder regularResourceBuilder;

    @Autowired
    WordEmbeddingBuilder wordEmbeddingBuilder;

    @Autowired
    UDM udm;

    @Value("${epnoi.comparator.threshold}")
    Double similarityThreshold;
}
