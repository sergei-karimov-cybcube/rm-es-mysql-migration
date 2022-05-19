package com.cybercube.rmesmysqlmigration.gateway.mysql;

import com.cybercube.rmesmysqlmigration.gateway.mysql.configuration.mySqlConnection;
import com.cybercube.rmesmysqlmigration.gateway.mysql.model.LossModelResultEntity;
import com.cybercube.rmesmysqlmigration.gateway.mysql.model.ThreatModelResultEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class AnalysisResultRepositoryImpl implements AnalysisResultRepository {
    @Override
    public List<UUID> getAllAnalysisResultIdByModelType(String modelType) {
        List<UUID> result = new ArrayList<>();
        try {
            Connection conn = mySqlConnection.connectDB();
            assert conn != null;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    String.format(
                            "select id from model_result where model_type = '%s' and analysis_id in (select an.id from analysis an where an.status = 'SUCCESS' and an.account_id in (select acc.id from account acc where deleted_at is NULL))",
                            modelType
                    )
            );

            while (rs.next()) {
                if (rs.getString(1) != null)
                    result.add(UUID.fromString(rs.getString(1)));
            }
            conn.close();
            return result;

        } catch (SQLException ex) {
            log.error(ex.getSQLState());
        }

        return Collections.emptyList();
    }

    @Override
    public List<UUID> getAllAssessmentId() {
        List<UUID> result = new ArrayList<>();
        try {
            Connection conn = mySqlConnection.connectDB();
            assert conn != null;
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "select assessment_id from answer where user_type = 'FINAL' and answers_dataset_id in (select answers_dataset.id from answers_dataset where used_in_analysis is TRUE and account_id in (select acc.id from account acc where deleted_at is NULL))"
            );

            while (rs.next()) {
                if (rs.getString(1) != null)
                    result.add(UUID.fromString(rs.getString(1)));
            }
            conn.close();
            return result;

        } catch (SQLException ex) {
            log.error(ex.getSQLState());
        }

        return Collections.emptyList();
    }

    @Override
    public LossModelResultEntity saveLossModelResult(LossModelResultEntity entity) {
        log.info("Try to insert to loss model result");
        log.info(entity.result.toString());
        try {
            Connection conn = mySqlConnection.connectDB();
            String json = new ObjectMapper().writeValueAsString(entity.result);
            assert conn != null;
            String query = "insert into loss_model_result (id, version, analysis_id, result_type, error_message, result) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, entity.id.toString());
            statement.setInt(2, 1);
            statement.setString(3, entity.analysisId.toString());
            statement.setString(4, entity.resultType.toString());
            statement.setString(5, entity.errorMessage);
            statement.setObject(6, json);
            statement.execute();
            conn.close();
            log.info("Finish insert");
            return entity;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            return null;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public ThreatModelResultEntity saveThreatModelResult(ThreatModelResultEntity entity) {
        log.info("Try to insert to threat model result");
        log.info(entity.result.toString());
        try {
            Connection conn = mySqlConnection.connectDB();
            assert conn != null;
            String json = new ObjectMapper().writeValueAsString(entity.result);
            String query = "insert into threat_model_result (id, version, analysis_id, result_type, error_message, result) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, entity.id.toString());
            statement.setInt(2, 1);
            statement.setString(3, entity.analysisId.toString());
            statement.setString(4, entity.resultType.toString());
            statement.setString(5, entity.errorMessage);
            statement.setObject(6, json);
            statement.execute();
            log.info("Finish insert");
            conn.close();
            return entity;
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            return null;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }
}
