package org.qubership.reporter.inspectors.impl.metadata;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;
import java.util.Map;

public class TopicAdded extends ARepositoryInspector {
    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        List<Object> topics = (List<Object>) repoMetaData.get("topics");
        if (topics != null) {
            StringBuilder sb = new StringBuilder();
            for (Object topicName : topics) {
                sb.append(topicName).append("&nbsp;");
            }

            return info(sb.toString());
        }

        return info("&nbsp;");
    }

    @Override
    public String getMetricName() {
        return "Topics";
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Topics - are labels assigned to repositories in the github.com";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.META_DATA_GROUP;
    }
}
