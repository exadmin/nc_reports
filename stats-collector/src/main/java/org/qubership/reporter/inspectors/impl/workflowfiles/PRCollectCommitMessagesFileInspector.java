package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;
import org.qubership.reporter.model.MetricGroup;

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
    public String getMetricName() {
        return "WF/AddCommitMsg";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }
}
