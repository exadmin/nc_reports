package org.qubership.reporter.inspectors.impl.codequality;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.inspectors.api.ResultSeverity;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;
import java.util.Map;

public class LastUpdatedTime extends ARepositoryInspector {
    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        final String rawValue = (String) repoMetaData.get("updated_at");
        int tInddex = rawValue.indexOf('T');

        String dateStr = rawValue.substring(0, tInddex);
        String timeStr = rawValue.substring(tInddex + 1, rawValue.length() - 1);

        OneMetricResult metricResult = new OneMetricResult(getMetricName(), ResultSeverity.INFO, dateStr);
        metricResult.setToolTipForGithubOnly(dateStr + " " + timeStr);

        return metricResult;
    }

    @Override
    public String getMetricName() {
        return "Updated At";
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Returns time when repository was updated last time";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.CODE_QUALITY_GROUP;
    }
}
