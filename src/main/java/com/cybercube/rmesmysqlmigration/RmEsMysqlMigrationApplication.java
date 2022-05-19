package com.cybercube.rmesmysqlmigration;

import com.cybercube.rmesmysqlmigration.gateway.es.LossModelResultRepository;
import com.cybercube.rmesmysqlmigration.gateway.es.ThreatModelResultRepository;
import com.cybercube.rmesmysqlmigration.gateway.es.model.LossModelResult;
import com.cybercube.rmesmysqlmigration.gateway.es.model.ThreatModelResult;
import com.cybercube.rmesmysqlmigration.gateway.mysql.AnalysisResultRepository;
import com.cybercube.rmesmysqlmigration.gateway.mysql.model.LossModelResultEntity;
import com.cybercube.rmesmysqlmigration.gateway.mysql.model.ThreatModelResultEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@EnableElasticsearchRepositories
@RequiredArgsConstructor
@SpringBootApplication
@Slf4j
public class RmEsMysqlMigrationApplication implements CommandLineRunner {
    private final AnalysisResultRepository analysisResultRepository;
    private final LossModelResultRepository lossModelResultRepository;
    private final ThreatModelResultRepository threatModelResultRepository;

    public static void main(String[] args) {
        SpringApplication.run(RmEsMysqlMigrationApplication.class, args).close();
    }

    @Override
    public void run(String... args) {
        log.info("Start application");

        var sqlLossModelResultIds = fetchLossModelResults();
        var esLossModelResults = fetchLossModelResultsFromESByIds(sqlLossModelResultIds);
        var lossEntities = fetchLossModelResultEntities(esLossModelResults);
        var lossResults = lossEntities.stream().map(it -> analysisResultRepository.saveLossModelResult(it) == null).collect(Collectors.toList());
        log.info("All records: {}, failed records: {}", lossResults.size(), lossResults.stream().filter(it -> it).count());

        var sqlThreatModelResultIds = fetchThreatModelResults();
        var esThreatModelResults = fetchThreatModelResultsFromESByIds(sqlThreatModelResultIds);
        var threatEntities = fetchThreatModelResultEntities(esThreatModelResults);
        var threatResults = threatEntities.stream().map(it -> analysisResultRepository.saveThreatModelResult(it) == null).collect(Collectors.toList());
        log.info("All records: {}, failed records: {}", threatResults.size(), threatResults.stream().filter(it -> it).count());

        List<UUID> assessmentIds = analysisResultRepository.getAllAssessmentId();
        log.info("MySQL Assessment: " + assessmentIds.size());
        log.info("Finish application");


    }

    private List<ThreatModelResultEntity> fetchThreatModelResultEntities(List<ThreatModelResult> esThreatModelResults) {
        var threatModelResultEntities = esThreatModelResults
                .stream()
                .map(it -> ThreatModelResultEntity
                        .builder()
                        .id(it.getUuid())
                        .analysisId(it.getAnalysisId())
                        .resultType(it.getResultType())
                        .errorMessage(it.getErrorMessage())
                        .result(it.getResult())
                        .build()
                ).collect(Collectors.toList());
        log.info("MySQL Threat Entities: " + threatModelResultEntities.size());
        return threatModelResultEntities;
    }

    private List<LossModelResultEntity> fetchLossModelResultEntities(List<LossModelResult> esLossModelResults) {
        var lossModelResultEntities = esLossModelResults
                .stream()
                .map(it -> LossModelResultEntity
                        .builder()
                        .id(it.getUuid())
                        .analysisId(it.getAnalysisId())
                        .resultType(it.getResultType())
                        .errorMessage(it.getErrorMessage())
                        .result(it.getResult())
                        .build()
                ).collect(Collectors.toList());
        log.info("MySQL Loss Entities: " + lossModelResultEntities.size());
        return lossModelResultEntities;
    }

    private List<ThreatModelResult> fetchThreatModelResultsFromESByIds(List<UUID> sqlThreatModelResultIds) {
        List<ThreatModelResult> threatModelResults = sqlThreatModelResultIds
                .stream()
                .parallel()
                .map(id -> threatModelResultRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        log.info("ES Threat: " + threatModelResults.size());
        return threatModelResults;
    }

    private List<UUID> fetchThreatModelResults() {
        List<UUID> threatModelResultIds = analysisResultRepository.getAllAnalysisResultIdByModelType("THREAT_MODEL");
        log.info("MySQL Threat: " + threatModelResultIds.size());
        return threatModelResultIds;
    }

    private List<LossModelResult> fetchLossModelResultsFromESByIds(List<UUID> lossModelResultIds) {
        List<LossModelResult> lossModelResults = lossModelResultIds
                .stream()
                .parallel()
                .map(id -> lossModelResultRepository.findById(id).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        log.info("ES Loss: " + lossModelResults.size());
        return lossModelResults;
    }

    private List<UUID> fetchLossModelResults() {
        List<UUID> lossModelResultIds = analysisResultRepository.getAllAnalysisResultIdByModelType("LOSS_MODEL");
        log.info("MySQL Loss: " + lossModelResultIds.size());
        return lossModelResultIds;
    }
}
