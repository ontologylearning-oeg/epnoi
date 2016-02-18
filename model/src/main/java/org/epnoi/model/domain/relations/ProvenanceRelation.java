package org.epnoi.model.domain.relations;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by cbadenes on 16/02/16.
 */
@ToString(callSuper = true)
@EqualsAndHashCode(of={"uri"}, callSuper = true)
public abstract class ProvenanceRelation extends Relation {

    @Getter
    protected Map<String, Double> provenances = new HashMap<>();

    @Override
    public Double getWeight() {
        return provenances.values().stream().mapToDouble(x -> x.doubleValue()).average().getAsDouble();
    }

    public void add(String provenance, double relationProbability) {
        this.provenances.put(provenance, relationProbability);
    }

    public void addAll(Map<String, Double> provenance ){
        provenances.putAll(provenance);
    }

}
