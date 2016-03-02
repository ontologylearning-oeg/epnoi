package org.epnoi.storage.system.graph.queries;

import org.apache.commons.beanutils.BeanUtils;
import org.epnoi.model.domain.relations.DealsWithFromDocument;
import org.epnoi.model.domain.relations.DealsWithFromItem;
import org.epnoi.model.domain.relations.Relation;
import org.epnoi.model.domain.resources.Resource;
import org.epnoi.storage.system.graph.domain.nodes.DocumentNode;
import org.epnoi.storage.system.graph.domain.nodes.ItemNode;
import org.epnoi.storage.system.graph.domain.nodes.TopicNode;
import org.neo4j.ogm.session.result.QueryStatistics;
import org.neo4j.ogm.session.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by cbadenes on 28/02/16.
 */
@Component
public class DealsItemGraphQuery implements GraphQuery<DealsWithFromItem> {

    private static final Logger LOG = LoggerFactory.getLogger(DealsItemGraphQuery.class);

    @Autowired
    GraphQueryExecutor executor;

    @Override
    public Relation.Type accept() {
        return Relation.Type.DEALS_WITH_FROM_ITEM;
    }

    @Override
    public List<DealsWithFromItem> query(String startUri, String endUri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",startUri);
        params.put("1",endUri);
        Result result = executor.query("match (node1:Item{uri:{0}})-[r:DEALS_WITH]->(node2:Topic{uri:{1}}) return r", params);

        List<DealsWithFromItem> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map values = (Map) it.next().get("r");
                DealsWithFromItem similarTo = new DealsWithFromItem();
                BeanUtils.populate(similarTo,values);
                similarTo.setStartUri(startUri);
                similarTo.setEndUri(endUri);
                similars.add(similarTo);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting deals_with relation between: " + startUri +" and " + endUri,e);
            }
        }
        return similars;
    }

    @Override
    public List<DealsWithFromItem> inDomain(String uri) {
        Map<String,String> params = new HashMap<>();
        params.put("0",uri);
        Result result = executor.query("match (domain:Domain{uri:{0}})-[c:CONTAINS]->(d:Document)-[:BUNDLES]->(i:Item)-[r:DEALS_WITH]->(t:Topic) return r,i,t", params);

        List<DealsWithFromItem> similars = new ArrayList<>();

        Iterator<Map<String, Object>> it = result.queryResults().iterator();
        while(it.hasNext()){
            try {
                Map<String, Object> map = it.next();

                Map rValues = (Map) map.get("r");
                DealsWithFromItem dealsWith = new DealsWithFromItem();
                BeanUtils.populate(dealsWith,rValues);

                Map iValues = (Map) map.get("i");
                ItemNode iNode = new ItemNode();
                BeanUtils.populate(iNode,iValues);

                Map tValues = (Map) map.get("t");
                TopicNode tNode = new TopicNode();
                BeanUtils.populate(tNode,tValues);

                dealsWith.setStartUri(iNode.getUri());
                dealsWith.setEndUri(tNode.getUri());

                similars.add(dealsWith);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.error("Error getting deals_with for item relation from: " + uri ,e);
            }
        }
        return similars;
    }

    @Override
    public void deleteIn(Resource.Type type, String uri) {
        Map<String,Object> params = new HashMap<>();
        params.put("0",uri);

        String query;
        switch (type){
            case DOMAIN:
                query = "match (domain{uri:{0}})-[c:CONTAINS]->(d:Document)-[b:BUNDLES]->(i:Item)-[r:DEALS_WITH]->(t:Topic) delete r";
                break;
            default: query = "";
        }

        QueryStatistics result = executor.execute(query, params);
        LOG.info("Result of query execution ["+ query + "] is: " + result);
    }
}
