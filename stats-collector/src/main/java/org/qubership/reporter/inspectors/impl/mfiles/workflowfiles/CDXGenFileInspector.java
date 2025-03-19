package org.qubership.reporter.inspectors.impl.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.model.MetricGroup;

import java.util.List;

public class CDXGenFileInspector extends AGithubWorkflowFileInspector {
    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "cdxgen.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
        // sha256CheckSums.add("28nGOOhUtjDidPWgYahboIBwuzHVgUkeMUAcDicaD20=");
    }

    @Override
    public String getMetricName() {
        return "WF/CycloneDX";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*netcracker/qubership-workflow-hub/actions/cdxgen@");
    }
}
