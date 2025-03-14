package org.qubership.reporter.inspectors.impl.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class SuperLinterFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "super-linter.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        // sha256CheckSums.add("sDiYyYY527ERa/+6s7WBaoVt7PxZIXc2DtcIoQ5rR/E=");
    }

    @Override
    public String getMetricName() {
        return "WF/SuperLinter";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }

    @Override
    protected void addExpectedContentRegExpressions(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*super-linter/super-linter@");
    }
}
