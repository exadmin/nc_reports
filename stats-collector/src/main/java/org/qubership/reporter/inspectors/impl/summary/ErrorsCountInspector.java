package org.qubership.reporter.inspectors.impl.summary;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.model.MetricGroup;
import org.qubership.reporter.model.ReservedColumns;

import java.util.List;
import java.util.Map;

public class ErrorsCountInspector extends ARepositoryInspector {
    @Override
    public String getMetricName() {
        return ReservedColumns.ERRORS_COUNT_THIS_WEEK;
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.EXECUTIVE_SUMMARY;
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "";
    }

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        return info("tbd");
    }
}
