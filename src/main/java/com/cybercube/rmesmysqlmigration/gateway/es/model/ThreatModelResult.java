package com.cybercube.rmesmysqlmigration.gateway.es.model;

import com.cybercube.rmesmysqlmigration.gateway.core.model.ModelResult;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "rm-threat-model-index")
public class ThreatModelResult extends ModelResult {
}