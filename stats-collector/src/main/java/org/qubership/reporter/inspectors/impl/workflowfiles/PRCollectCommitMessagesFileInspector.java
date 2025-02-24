package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

import java.util.List;

public class PRCollectCommitMessagesFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "pr-collect-commit-messages.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("Ds4rRTAH2rEcSaAA4QBiYLdeZnOu2DbwzXeHGgfEz/A=");
    }

    @Override
    protected String getMetricName() {
        return "WF/AddCommitMsg";
    }
}
