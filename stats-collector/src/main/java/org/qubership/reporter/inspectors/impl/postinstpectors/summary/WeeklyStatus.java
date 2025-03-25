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
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * Prepares last X weeks summary status
 */
public class WeeklyStatus extends AbstractPostInspector {
    private static final int X_WEEKS_AGO = 12;
    private static final DateTimeFormatter DB_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter UI_FORMAT = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
    private static final String SQL_QUERY = "SELECT max(\"Errors Count\") FROM nc_reports WHERE repository_name = ? AND report_date BETWEEN ? AND ?";

    private Connection jdbcConnection;

    @Override
    public void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData, Connection jdbcConnection) {
        this.jdbcConnection = jdbcConnection;

        LocalDateTime prevSunday = getPrevSunday();
        prevSunday = prevSunday.minusWeeks(X_WEEKS_AGO);

        LocalDateTime[] sundays = new LocalDateTime[X_WEEKS_AGO];
        for (int i=0; i<X_WEEKS_AGO; i++) {
            sundays[i] = prevSunday;
            prevSunday = prevSunday.plusWeeks(1);
        }

        for (int i=1; i<X_WEEKS_AGO; i++) {
            createColumnForWeek(sundays[i-1], sundays[i], reportModel);
        }
    }

    @Override
    public Metric getMetric() {
        return null;
    }

    private static LocalDateTime getPrevSunday() {
        LocalDateTime today = LocalDateTime.now();

        LocalDateTime sunday = today.with(DayOfWeek.SUNDAY);
        if (sunday.isAfter(today)) {
            sunday.minusWeeks(1);
        }

        return sunday;
    }

    private void createColumnForWeek(LocalDateTime fromDate, LocalDateTime toDate, ReportModel reportModel) {
        String fromDateStr = fromDate.format(DB_FORMAT);
        String toDateStr   = toDate.format(DB_FORMAT);

        String columnName  = fromDate.format(UI_FORMAT);
        Metric metric = new Metric(columnName, MetricType.NON_PERSISTENT, columnName, MetricGroupsRegistry.EXECUTIVE_SUMMARY);

        for (String repositoryName : reportModel.getRepositoryNames()) {
            List<String> list = JDBCUtils.doSingleColumnSelect(jdbcConnection, SQL_QUERY, repositoryName, fromDateStr, toDateStr);
            String result = list.get(0) == null ? "0" : list.get(0);

            OneMetricResult omResult = new OneMetricResult(metric, ResultSeverity.INFO, result);
            reportModel.addData(repositoryName, omResult);
        }
    }
}
