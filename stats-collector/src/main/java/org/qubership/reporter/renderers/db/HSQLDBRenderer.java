package org.qubership.reporter.renderers.db;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.utils.DateUtils;
import org.qubership.reporter.utils.JDBCUtils;
import org.qubership.reporter.utils.TheLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HSQLDBRenderer {
    public static final String SQL_GET_ALL_COLUMNS_IN_NC_REPO_TABLE = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA='PUBLIC' AND TABLE_NAME='NC_REPORTS'";

    public void saveToDB(Connection jdbcConnection, ReportModel report, String dateStrOverride) throws SQLException {
        ensureDB(jdbcConnection);



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
            for (String absentColumnName : nonExistedColumnNames) {
                JDBCUtils.executeDDL(jdbcConnection, "ALTER TABLE NC_REPORTS ADD COLUMN \"" + absentColumnName +"\" VARCHAR(255)");
            }
        }

        // define next REPORT_ID as a max value for this column
        int reportId = 0;
        List<String> tmpList = JDBCUtils.doSingleColumnSelect(jdbcConnection, "SELECT max(report_id) FROM nc_reports");
        if (!tmpList.isEmpty()) {
            String value = tmpList.get(0);
            if (value != null) reportId = Integer.parseInt(tmpList.get(0)) + 1;
        }

        // do data store
        StringBuilder sql = new StringBuilder("INSERT INTO NC_REPORTS (REPORT_DATE, REPORT_ID, REPOSITORY_NAME");
        StringBuilder binds = new StringBuilder("?, ?, ?");

        for (Metric metric : report.getMetrics()) {
            sql.append(", \"").append(metric.getPersistenceId()).append("\"");
            binds.append(", ?");
        }
        sql.append(") VALUES (").append(binds).append(")");

        String dateStr = dateStrOverride == null ? DateUtils.getCurrentDateTimeStamp() : dateStrOverride;

        try (PreparedStatement pstm = jdbcConnection.prepareStatement(sql.toString())) {

            for (String repoName : report.getRepositoryNames()) {
                pstm.setString(1, dateStr);
                pstm.setInt(2, reportId);
                pstm.setString(3, repoName);

                int bindIndex = 4;
                for (Metric metric : report.getMetrics()) {
                    OneMetricResult omrValue = report.getValue(repoName, metric.getPersistenceId());

                    String value = omrValue == null ? null : omrValue.getRawValue();
                    pstm.setString(bindIndex, value);
                    bindIndex++;
                }

                pstm.addBatch();
            }

            pstm.executeBatch();
        }

    }

    private void ensureDB(Connection jdbcConnection) {
        List<String> existedColumnNames = JDBCUtils.doSingleColumnSelect(jdbcConnection, SQL_GET_ALL_COLUMNS_IN_NC_REPO_TABLE);

        if (!existedColumnNames.contains("REPORT_ID")) {
            TheLogger.debug("DataBase does not exist. Creating default tables.");
            JDBCUtils.executeDDL(jdbcConnection, "CREATE TABLE NC_REPORTS(REPORT_DATE TIMESTAMP NOT NULL, REPORT_ID INT NOT NULL, REPOSITORY_NAME VARCHAR(255) NOT NULL)");
        } else {
            TheLogger.debug("DataBase is found.");
        }
    }

}
