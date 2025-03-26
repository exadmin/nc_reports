package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;

import java.util.List;

public class PRConventionalCommitsRequiredFileInspector extends AbstractGithubWorkflowRequiredFileInspector {
    @Override
    protected Metric getMetricWithDescription() {
        Metric metric = newMetric("WF/ConvCommits","ConvCommits", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
        metric.setDescriptionRef("https://github.com/Netcracker/qubership-workflow-hub?tab=readme-ov-file#conventional-commits-pr-check");
        return metric;
    }

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "pr-conventional-commits.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*webiny/action-conventional-commits@");
    }
}
