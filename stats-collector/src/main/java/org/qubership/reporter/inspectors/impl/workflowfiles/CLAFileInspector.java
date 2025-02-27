package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

import java.util.List;

public class CLAFileInspector extends AGithubWorkflowFileInspector {

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "cla.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("2NRyXrE8sP1riS2MyKfLoWjrm0OtQvfweBBOqUqtAfo=");
    }

    @Override
    public String getMetricName() {
        return "WF/CLA";
    }
}
