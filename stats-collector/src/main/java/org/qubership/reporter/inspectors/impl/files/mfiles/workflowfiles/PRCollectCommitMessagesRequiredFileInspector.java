package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;

import java.util.List;

public class PRCollectCommitMessagesRequiredFileInspector extends AbstractGithubWorkflowRequiredFileInspector {
    @Override
    protected Metric getMetricWithDescription() {
        Metric metric = newMetric("WF/AddCommitMsg","AddCommitMsg", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
        metric.setDescriptionRef("https://github.com/Netcracker/qubership-workflow-hub?tab=readme-ov-file#add-commit-messages-to-pr-body");
        return metric;
    }

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "pr-collect-commit-messages.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*netcracker/qubership-workflow-hub/actions/pr-add-messages@");
    }
}
