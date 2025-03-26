package org.qubership.reporter.inspectors.impl.codequality;

import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;

import java.util.List;
import java.util.Map;

public class LastUpdatedTime extends AbstractRepositoryInspector {
    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        final String rawValue = (String) repoMetaData.get("updated_at");
        int tInddex = rawValue.indexOf('T');

        String dateStr = rawValue.substring(0, tInddex);
        String timeStr = rawValue.substring(tInddex + 1, rawValue.length() - 1);

        return info(dateStr);
        // metricResult.setToolTipForGithubOnly(dateStr + " " + timeStr);
    }

    @Override
    public Metric getMetric() {
        return newMetric("Updated At", "Updated At", MetricGroupsRegistry.CODE_QUALITY_GROUP)
                .setDescriptionRef(""); // todo
    }
}
