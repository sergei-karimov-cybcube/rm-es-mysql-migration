package com.cybercube.rmesmysqlmigration.gateway.es;

import com.cybercube.rmesmysqlmigration.gateway.es.model.ThreatModelResult;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface ThreatModelResultRepository extends ElasticsearchRepository<ThreatModelResult, UUID> {
}
