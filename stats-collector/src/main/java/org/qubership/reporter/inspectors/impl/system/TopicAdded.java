package org.qubership.reporter.inspectors.impl.system;

import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.TextAlign;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;

import java.util.List;
import java.util.Map;

public class TopicAdded extends AbstractRepositoryInspector {

    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        OneMetricResult omResult = _inspectRepoFolder(pathToRepository, repoMetaData, allReposMetaData);
        omResult.setTextAlign(TextAlign.CENTER_MIDDLE);

        return omResult;
    }

    private OneMetricResult _inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        List<Object> topics = (List<Object>) repoMetaData.get("topics");
        if (topics == null || topics.isEmpty()) {
            return error("", null, "No topics are set");
        }

        StringBuilder sb = new StringBuilder();
        for (Object topicName : topics) {
            sb.append(topicName).append(" ");
        }

        return info(sb.toString());
    }

    @Override
    public Metric getMetric() {
        return newMetric("Topics", "Topics", MetricGroupsRegistry.EXECUTIVE_SUMMARY)
                // .setDescriptionRef("Topics - are labels assigned to repositories in the github.com")
                .setRenderingOrderWeight(-50);
    }
}
