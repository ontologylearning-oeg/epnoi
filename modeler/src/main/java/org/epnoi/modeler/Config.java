package org.epnoi.modeler;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.util.List;

/**
 * Created by cbadenes on 11/01/16.
 */
@Configuration("modeler")
@ComponentScan({"org.epnoi.modeler","org.epnoi.storage","es.upm.oeg.epnoi.ressist.parser","org.epnoi.eventbus"})
@PropertySource({"classpath:modeler.properties","classpath:eventbus.properties","classpath:storage.properties"})
public class Config {

    @Autowired
    List<RouteBuilder> builders;

    @Bean
    public SpringCamelContext camelContext(ApplicationContext applicationContext) throws Exception {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        for(RouteBuilder builder : builders){
            camelContext.addRoutes(builder);
        }
        return camelContext;
    }

    //To resolve ${} in @Value
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        return new PropertySourcesPlaceholderConfigurer();
    }

}