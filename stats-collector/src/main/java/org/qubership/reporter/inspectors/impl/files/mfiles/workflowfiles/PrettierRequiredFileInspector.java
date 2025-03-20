package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;

import java.util.List;

public class PrettierRequiredFileInspector extends AbstractGithubWorkflowRequiredFileInspector {
    @Override
    protected Metric getMetricWithoutDescription() {
        return newMetric("WF/Prettier", "Prettier", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
    }

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "prettier.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*Netcracker/qubership-workflow-hub/.github/workflows/prettierFix.yaml@");
    }
}
