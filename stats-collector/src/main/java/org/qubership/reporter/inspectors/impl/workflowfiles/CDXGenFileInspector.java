package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class CDXGenFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "cdxgen.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        sha256CheckSums.add("28nGOOhUtjDidPWgYahboIBwuzHVgUkeMUAcDicaD20=");
    }

    @Override
    public String getMetricName() {
        return "WF/CycloneDX";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }
}
