package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;

import java.util.List;

public class ProfanityFilterRequiredFileInspector extends AbstractGithubWorkflowRequiredFileInspector {
    @Override
    protected Metric getMetricWithDescription() {
        Metric metric = newMetric("WF/ProfanityFilter", "ProfanityFilter", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
        metric.setDescriptionRef("https://github.com/Netcracker/qubership-workflow-hub?tab=readme-ov-file#profanity-filter");
        return metric;
    }

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "profanity-filter.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*Netcracker/qubership-workflow-hub/.github/workflows/profanityFilter.yaml@");
    }
}
