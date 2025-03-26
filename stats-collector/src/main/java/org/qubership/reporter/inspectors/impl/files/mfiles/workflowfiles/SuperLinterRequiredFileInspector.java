package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;

import java.util.List;

public class SuperLinterRequiredFileInspector extends AbstractGithubWorkflowRequiredFileInspector {
    @Override
    protected Metric getMetricWithDescription() {
        Metric metric = newMetric("WF/SuperLinter", "SuperLinter", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
        metric.setDescriptionRef("https://github.com/Netcracker/qubership-workflow-hub?tab=readme-ov-file#lint-code-base-super-linter");
        return metric;
    }

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "super-linter.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        // sha256CheckSums.add("sDiYyYY527ERa/+6s7WBaoVt7PxZIXc2DtcIoQ5rR/E=");
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*super-linter/super-linter@");
    }
}
