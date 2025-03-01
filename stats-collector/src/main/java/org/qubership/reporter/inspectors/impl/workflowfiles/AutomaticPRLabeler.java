package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

import java.util.List;

public class AutomaticPRLabeler extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "automatic-pr-labeler.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("LXd62wzKhH05wia6N7v4f4Use5q1Uwk0QLDABoyXMLg=");
    }

    @Override
    public String getMetricName() {
        return "WF/Labeler";
    }
}
