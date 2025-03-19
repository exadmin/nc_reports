package org.qubership.reporter.inspectors.impl.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class AutomaticPRLabeler extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "automatic-pr-labeler.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        // sha256CheckSums.add("LXd62wzKhH05wia6N7v4f4Use5q1Uwk0QLDABoyXMLg=");
    }

    @Override
    public String getMetricName() {
        return "WF/Labeler";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*Netcracker/qubership-workflow-hub/.github/workflows/auto-labeler.yaml@");
    }
}
