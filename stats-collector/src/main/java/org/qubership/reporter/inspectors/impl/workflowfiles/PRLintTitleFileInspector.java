package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class PRLintTitleFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "pr-lint-title.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("zgC/zLSkfoE5QpLe+EoT16wajIE5fMGn+af3zVCgKNI=");
    }

    @Override
    public String getMetricName() {
        return "WF/Lint-Title";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }
}
