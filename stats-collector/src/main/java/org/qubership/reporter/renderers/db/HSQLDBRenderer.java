package org.qubership.reporter.renderers.db;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.utils.DateUtils;
import org.qubership.reporter.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class HSQLDBRenderer {
    public static final String SQL_GET_ALL_COLUMNS_IN_NC_REPO_TABLE = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='NC_REPORTS'";

    public void saveToDB(Connection jdbcConnection, ReportModel report) throws SQLException {
        List<String> existedColumnNames = JDBCUtils.doSingleColumnSelect(jdbcConnection, SQL_GET_ALL_COLUMNS_IN_NC_REPO_TABLE);
        List<String> nonExistedColumnNames = new ArrayList<>();

        // ensure each metric has it's column in the DB
        for (Metric metric : report.getMetrics()) {
            String metricName = metric.getPersistenceId();

            if (!existedColumnNames.contains(metricName)) {
                nonExistedColumnNames.add(metricName);
            }
        }

        if (!nonExistedColumnNames.isEmpty()) {
            try (Statement ddlStm = jdbcConnection.createStatement()) {
                for (String absentColumnName : nonExistedColumnNames) {
                    ddlStm.execute("ALTER TABLE NC_REPORTS ADD COLUMN \"" + absentColumnName +"\" VARCHAR(255)");
                }
            }
        }

        // do data store
        StringBuilder sql = new StringBuilder("INSERT INTO NC_REPORTS (DATE, REPO_NAME");
        StringBuilder binds = new StringBuilder("?, ?");

        for (Metric metric : report.getMetrics()) {
            sql.append(", \"").append(metric.getPersistenceId()).append("\"");
            binds.append(", ?");
        }
        sql.append(") VALUES (").append(binds).append(")");

        String dateStr = DateUtils.getCurrentDateTimeStamp();

        try (PreparedStatement pstm = jdbcConnection.prepareStatement(sql.toString())) {

            for (String repoName : report.getRepositoryNames()) {
                pstm.setString(1, dateStr);
                pstm.setString(2, repoName);

                System.out.println("SQL QUERY = " + sql);

                int bindIndex = 3;
                for (Metric metric : report.getMetrics()) {
                    String value = report.getValue(repoName, metric.getPersistenceId()).getRawValue();
                    System.out.println("Assign bind index = " + bindIndex + ", raw-str-value = " +  value);
                    pstm.setString(bindIndex, value);
                    bindIndex++;
                }

                pstm.addBatch();
            }

            pstm.executeBatch();
        }

    }
}
