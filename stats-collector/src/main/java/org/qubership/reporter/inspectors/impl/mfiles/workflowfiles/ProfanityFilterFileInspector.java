package org.qubership.reporter.inspectors.impl.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class ProfanityFilterFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "profanity-filter.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        // sha256CheckSums.add("f8NYnS/VlsMgAOlFkEdF1u7cjajSBzzZDSul2RjQP1I=");
    }

    @Override
    public String getMetricName() {
        return "WF/ProfanityFilter";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }

    @Override
    protected void addExpectedContentRegExpressions(List<String> strings) {
        strings.add("\\buses\\s*:\\s*Netcracker/qubership-workflow-hub/.github/workflows/profanityFilter.yaml@");
    }
}
