package org.qubership.reporter.inspectors.impl.postinstpectors.summary;

import org.qubership.reporter.inspectors.api.AbstractPostInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.metric.MetricType;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.utils.JDBCUtils;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public class DailyStatus extends AbstractPostInspector {
    // Number of days to show statistics for
    private static final int X_COLUMNS_TO_SHOW = 14;

    @Override
    public void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData, Connection jdbc) {
        // select current max report
        int currentReportId = JDBCUtils.selectInt(jdbc, "SELECT max(report_id) from nc_reports");
        int producedColumns = 0;

        while (currentReportId > 0 && producedColumns < X_COLUMNS_TO_SHOW) {
            // check if report with current id exits in the database
            int reportId = JDBCUtils.selectInt(jdbc, "select distinct report_id from nc_reports where report_id = ?", currentReportId + "");
            if (reportId != currentReportId) {
                currentReportId--;
                continue;
            }

            createColumn(jdbc, reportId, reportModel, producedColumns);
            producedColumns++;
            currentReportId--;
        }
    }

    @Override
    public Metric getMetric() {
        return null;
    }

    private static final String SQL_QUERY = "SELECT max(\"Errors Count\") FROM nc_reports WHERE report_id = ? and repository_name = ?";

    private void createColumn(Connection jdbc, int reportId, ReportModel reportModel, int order) {
        String columnName = "Run #" + reportId;
        Metric metric = new Metric(columnName, MetricType.NON_PERSISTENT, columnName, MetricGroupsRegistry.EXECUTIVE_SUMMARY);
        metric.setRenderingOrderWeight(50 - order * 2);

        for (String repositoryName : reportModel.getRepositoryNames()) {
            int errorsCount = JDBCUtils.selectInt(jdbc, SQL_QUERY,  reportId, repositoryName);

            OneMetricResult omResult = new OneMetricResult(metric, ResultSeverity.INFO, "" + errorsCount);
            reportModel.addData(repositoryName, omResult);
        }
    }
}