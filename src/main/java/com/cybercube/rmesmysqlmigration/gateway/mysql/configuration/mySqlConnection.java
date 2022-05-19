package com.cybercube.rmesmysqlmigration.gateway.mysql.configuration;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class mySqlConnection {
    public static Connection connectDB() {
        try {
            return DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/rm_backend?useUnicode=true&characterEncoding=UTF-8",
                    "rm", "rm_pass");
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
