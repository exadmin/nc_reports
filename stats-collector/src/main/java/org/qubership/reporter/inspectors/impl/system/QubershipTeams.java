package org.qubership.reporter.inspectors.impl.system;

import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.utils.StrUtils;

import java.util.List;
import java.util.Map;

public class QubershipTeams extends AbstractRepositoryInspector {
    @Override
    protected List<OneMetricResult> inspectRepoFolderWithManyMetrics(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        return List.of(inspectRepoFolder(pathToRepository, repoMetaData, allReposMetaData));
    }

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        List<Object> topics = (List<Object>) repoMetaData.get("topics");
        if (topics == null || topics.isEmpty()) {
            return error("", null, "No topics are set");
        }

        for (Object topicName : topics) {
            if (topicName == null) continue;
            String topicNameStr = topicName.toString();
            if (StrUtils.isEmpty(topicNameStr)) continue;

            String tmpName = topicNameStr.toUpperCase();
            if (tmpName.startsWith("QUBERSHIP-")) {
                return ok(topicNameStr);
            }
        }

        return error("");
    }

    @Override
    public Metric getMetric() {
        return newMetric("QubershipTeam", "Team", MetricGroupsRegistry.EXECUTIVE_SUMMARY)
                .setRenderingOrderWeight(-75)
                .setRenderOnEachReportTab(true);
    }
}
