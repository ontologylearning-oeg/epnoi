package org.epnoi.storage.system.column.repository;


import org.epnoi.model.domain.resources.Resource;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Created by cbadenes on 21/12/15.
 */
@NoRepositoryBean
public interface BaseColumnRepository<T extends Resource> extends CassandraRepository<T> {

}
