package org.qubership.reporter.inspectors.impl.postinstpectors.summary;

import org.qubership.reporter.inspectors.api.AbstractPostInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.metric.MetricType;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ErrorsCountInspector extends AbstractPostInspector {

    @Override
    public void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData, Connection jdbcConnection) {
        // define virtual column with executive results
        for (String repositoryName : reportModel.getRepositoryNames()) {
            int errCount = 0;

            for (Metric metric : reportModel.getMetrics()) {
                if (MetricType.PERSISTENT.equals(metric.getType())) {
                    OneMetricResult omResult = reportModel.getValue(repositoryName, metric.getPersistenceId());
                    ResultSeverity resultSeverity = omResult.getSeverity();

                    if (resultSeverity.equals(ResultSeverity.ERROR) || resultSeverity.equals(ResultSeverity.WARN) || resultSeverity.equals(ResultSeverity.SECURITY_ISSUE)) {
                        errCount++;
                    }
                }
            }

            OneMetricResult omRes = info("" + errCount);
            reportModel.addData(repositoryName, Collections.singletonList(omRes));
        }
    }

    @Override
    public Metric getMetric() {
        return newMetric("Errors Count", "Errors Count", MetricGroupsRegistry.EXECUTIVE_SUMMARY)
                .setDescriptionRef("")
                .setRenderingOrderWeight(+100);
    }
}
