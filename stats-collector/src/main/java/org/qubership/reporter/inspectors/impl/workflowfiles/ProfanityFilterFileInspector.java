package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

import java.util.List;

public class ProfanityFilterFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "profanity-filter.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("f8NYnS/VlsMgAOlFkEdF1u7cjajSBzzZDSul2RjQP1I=");
    }

    @Override
    public String getMetricName() {
        return "WF/ProfanityFilter";
    }
}
