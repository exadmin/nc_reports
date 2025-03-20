package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;

import java.util.List;

public class PRConventionalCommitsRequiredFileInspector extends AbstractGithubWorkflowRequiredFileInspector {
    @Override
    protected Metric getMetricWithoutDescription() {
        return newMetric("WF/ConvCommits","ConvCommits", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
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
