package org.qubership.reporter.inspectors.impl.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class PRCollectCommitMessagesFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "pr-collect-commit-messages.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        // sha256CheckSums.add("Ds4rRTAH2rEcSaAA4QBiYLdeZnOu2DbwzXeHGgfEz/A=");
    }

    @Override
    public String getMetricName() {
        return "WF/AddCommitMsg";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*netcracker/qubership-workflow-hub/actions/pr-add-messages@");
    }
}
