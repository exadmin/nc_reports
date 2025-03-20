package org.qubership.reporter.inspectors.impl.system;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;

import java.util.List;
import java.util.Map;

public class TopicAdded extends AbstractRepositoryInspector {
    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        List<Object> topics = (List<Object>) repoMetaData.get("topics");
        if (topics != null) {
            StringBuilder sb = new StringBuilder();
            for (Object topicName : topics) {
                sb.append(topicName).append("<br>");
            }

            return info(sb.toString());
        }

        return info("&nbsp;");
    }

    @Override
    public Metric getMetric() {
        return newMetric("Topics", "Topics", MetricGroupsRegistry.SYSTEM_METRIC_GROUP)
                .setDescription("Topics - are labels assigned to repositories in the github.com");
    }
}
