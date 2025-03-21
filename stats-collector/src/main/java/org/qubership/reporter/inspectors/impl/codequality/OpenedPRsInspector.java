package org.qubership.reporter.inspectors.impl.codequality;

import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;

import java.util.List;
import java.util.Map;

public class OpenedPRsInspector extends AbstractRepositoryInspector {
    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        return warn("no data yet");
    }

    @Override
    public Metric getMetric() {
        return newMetric("OpenedPRs", "Open Pull Requests Count", MetricGroupsRegistry.CODE_QUALITY_GROUP)
                .setDescription("");
    }
}
