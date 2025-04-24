package org.qubership.reporter.inspectors.impl.system;

import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.TextAlign;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;

import java.util.List;
import java.util.Map;

public class RepoIDInspector extends AbstractRepositoryInspector {
    @Override
    protected List<OneMetricResult> inspectRepoFolderWithManyMetrics(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        return List.of(inspectRepoFolder(pathToRepository, repoMetaData, allReposMetaData));
    }

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        String repoName = (String) repoMetaData.get("name");
        String url = (String) repoMetaData.get("html_url");


        OneMetricResult metricResult = info(repoName);
        metricResult.setHttpReference(url);
        metricResult.setTextAlign(TextAlign.LEFT_MIDDLE);

        return metricResult;
    }

    @Override
    public Metric getMetric() {
        return newMetric("ID", "ID", MetricGroupsRegistry.SYSTEM_METRIC_GROUP)
                .setDescriptionRef("") // todo
                .setRenderingOrderWeight(-100)
                .setRenderOnEachReportTab(true);
    }
}
