package com.cybercube.rmesmysqlmigration.gateway.core.model;

import javax.validation.constraints.NotNull;

import com.cybercube.rmesmysqlmigration.gateway.es.model.AnalysisResultType;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;

import java.util.Map;
import java.util.UUID;

@Data
public class ModelResult {
    @Id
    @NotNull
    private UUID uuid;

    @NotNull
    @Field
    private UUID analysisId;

    @NotNull
    @Field
    private AnalysisResultType resultType;

    @Field
    private String errorMessage;

    @Field
    private Map<String, Object> result;
}
