package com.cybercube.rmesmysqlmigration.gateway.es.model;

import com.cybercube.rmesmysqlmigration.gateway.core.model.ModelResult;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "rm-loss-model-index")
public class LossModelResult extends ModelResult {
}