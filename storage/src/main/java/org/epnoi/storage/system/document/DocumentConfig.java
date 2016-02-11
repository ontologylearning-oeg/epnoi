package org.epnoi.storage.system.document;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;

/**
 * Created by cbadenes on 21/12/15.
 */
@Configuration
@ComponentScan({"org.epnoi.storage.system.document.repository"})
@EnableElasticsearchRepositories(basePackages = {"org.epnoi.storage.system.document.repository"})
@EnableTransactionManagement
public class DocumentConfig {

    @Autowired
    private Environment env;

    @Bean
    public TransportClient client(){


        Settings settings = ImmutableSettings.settingsBuilder().
//                        put("cluster.name", "drinventor").
                        put("client.transport.sniff", false).
                        put("client.transport.ping_timeout", 30, TimeUnit.SECONDS).
                build();

        TransportClient client = new TransportClient(settings);
        TransportAddress address = new InetSocketTransportAddress(env.getProperty("epnoi.elasticsearch.contactpoints"),Integer.parseInt(env.getProperty("epnoi.elasticsearch.port")));
        client.addTransportAddress(address);
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() {
        return new ElasticsearchTemplate(client());
    }
}
