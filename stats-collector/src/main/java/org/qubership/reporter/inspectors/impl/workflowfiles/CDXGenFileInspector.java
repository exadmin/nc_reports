package org.qubership.reporter.inspectors.impl.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AGithubWorkflowFileInspector;

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
    protected String getMetricName() {
        return "WF/CycloneDX";
    }
}
