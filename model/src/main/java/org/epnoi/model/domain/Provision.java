package org.epnoi.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by cbadenes on 21/01/16.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(of = "uri", callSuper = true)
public class Provision extends Resource {

    public static final String SOURCE="source";
    String source;

    public static final String DOCUMENT="document";
    String document;

    public static final String DATE="date";
    String date;
}
