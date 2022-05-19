package com.cybercube.rmesmysqlmigration.gateway.mysql;

import com.cybercube.rmesmysqlmigration.gateway.es.model.ThreatModelResult;
import com.cybercube.rmesmysqlmigration.gateway.mysql.model.LossModelResultEntity;
import com.cybercube.rmesmysqlmigration.gateway.mysql.model.ThreatModelResultEntity;

import java.util.List;
import java.util.UUID;

public interface AnalysisResultRepository {
    List<UUID> getAllAnalysisResultIdByModelType(String modelType);
    List<UUID> getAllAssessmentId();
    LossModelResultEntity saveLossModelResult(LossModelResultEntity entity);
    ThreatModelResultEntity saveThreatModelResult(ThreatModelResultEntity entity);
}
