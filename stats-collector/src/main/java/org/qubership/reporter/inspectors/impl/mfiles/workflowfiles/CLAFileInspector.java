package org.qubership.reporter.inspectors.impl.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class CLAFileInspector extends AGithubWorkflowFileInspector {

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "cla.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        // sha256CheckSums.add("2NRyXrE8sP1riS2MyKfLoWjrm0OtQvfweBBOqUqtAfo=");
    }

    @Override
    public String getMetricName() {
        return "WF/CLA";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*Netcracker/qubership-workflow-hub/.github/workflows/cla.yaml@");
    }
}
