package org.qubership.reporter.inspectors.impl.postinstpectors.codequality;

import org.qubership.reporter.inspectors.api.AbstractPostInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.inspectors.impl.codequality.CodeCoverageBySonar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SonarMetricAsInfoText extends AbstractPostInspector {
    private static final String CODE_COVERAGE_ID = new CodeCoverageBySonar().getMetric().getPersistenceId();

    @Override
    public void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData) {
        Map<String, OneMetricResult> newData = new HashMap<>();

        for (String repoName : reportModel.getRepositoryNames()) {
            for (Metric metric : reportModel.getMetrics()) {
                if (metric.getPersistenceId().equals(CODE_COVERAGE_ID)) {
                    OneMetricResult omResult = reportModel.getValue(repoName, metric.getPersistenceId());
                    String rawValue = omResult.getRawValue();

                    if (rawValue.endsWith("HTTP_CODE = 404")) rawValue = "0";

                    newData.put(repoName, new OneMetricResult(getMetric(), ResultSeverity.INFO, rawValue));
                }
            }
        }

        for (Map.Entry<String, OneMetricResult> me : newData.entrySet()) {
            reportModel.addData(me.getKey(), me.getValue());
        }
    }

    @Override
    public Metric getMetric() {
        return newMetric("sonar-coverage-metric-as-number", "Code Coverage %", MetricGroupsRegistry.CODE_QUALITY_GROUP);
    }
}
