package com.cybercube.rmesmysqlmigration.gateway.mysql.model;

import com.cybercube.rmesmysqlmigration.gateway.es.model.AnalysisResultType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Builder
public class ThreatModelResultEntity {
    public UUID id;
    public UUID analysisId;
    public AnalysisResultType resultType;
    public String errorMessage;
    public Map<String, Object> result;
}
