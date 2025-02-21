package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorResult;

import java.io.File;
import java.util.List;
import java.util.Map;

public class TopicAdded extends ARepositoryInspector {
    @Override
    protected InspectorResult inspectRepoFolder(String pathToRepository, List<Map<String, Object>> metaData) throws Exception {
        File repoDir = new File(pathToRepository);
        String expCloneUrl = "https://github.com/Netcracker/" + repoDir.getName() + ".git";

        for (Map<String, Object> next : metaData) {
            if (expCloneUrl.equals(next.get("clone_url"))) {
                List<Object> topics = (List<Object>) next.get("topics");
                if (topics != null) {
                    StringBuilder sb = new StringBuilder();
                    for (Object topicName : topics) {
                        sb.append(topicName).append("&nbsp;");
                    }

                    return info(sb.toString());
                }
            }
        }

        return info("&nbsp;");
    }

    @Override
    protected String getMetricName() {
        return "Topics";
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Topics - are labels assigned to repositories in the github.com";
    }
}
