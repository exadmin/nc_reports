package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.inspectors.api.ResultSeverity;
import org.qubership.reporter.model.MetricGroup;
import org.qubership.reporter.model.ReservedColumns;

import java.util.List;
import java.util.Map;

public class RepoIDInspector extends ARepositoryInspector {
    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        String repoName = (String) repoMetaData.get("name");
        String url = (String) repoMetaData.get("html_url");

        OneMetricResult metricResult = new OneMetricResult(ReservedColumns.ID, ResultSeverity.INFO, repoName);
        metricResult.setHttpReference(url);

        return metricResult;
    }

    @Override
    public String getMetricName() {
        return ReservedColumns.ID;
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.SYSTEM_METRIC_GROUP;
    }
}
