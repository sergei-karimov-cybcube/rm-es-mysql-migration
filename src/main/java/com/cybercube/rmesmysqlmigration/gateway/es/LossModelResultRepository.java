package com.cybercube.rmesmysqlmigration.gateway.es;

import com.cybercube.rmesmysqlmigration.gateway.es.model.LossModelResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface LossModelResultRepository extends ElasticsearchRepository<LossModelResult, UUID> {
}
