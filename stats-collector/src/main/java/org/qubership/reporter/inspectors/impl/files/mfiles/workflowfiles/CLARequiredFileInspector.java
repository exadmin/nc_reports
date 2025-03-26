package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.utils.RepoUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CLARequiredFileInspector extends AbstractGithubWorkflowRequiredFileInspector {
    @Override
    protected Metric getMetricWithDescription() {
        Metric metric = newMetric("WF/CLA","CLA", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
        metric.setDescriptionRef("https://github.com/Netcracker/qubership-workflow-hub?tab=readme-ov-file#cla");
        return metric;
    }

    @Override
    protected String getShortFileNamePlacedInGitHubWorkflowFolder() {
        return "cla.yaml";
    }

    @Override
    protected void addExpectedSha256Sums(List<String> sha256CheckSums) {
    }

    @Override
    protected void addExpectedContentRegExps(List<String> regExps) {
        regExps.add("\\buses\\s*:\\s*Netcracker/qubership-workflow-hub/.github/workflows/cla.yaml@");
    }

    @Override
    protected String checkForExpectedContentOrReturnErrorMsg(String content, List<Pattern> regExps, Map<String, Object> repoMetaData) {
        String repoName = RepoUtils.getRepositoryName(repoMetaData);
        if ("qubership-workflow-hub".equals(repoName)) return null;

        return super.checkForExpectedContentOrReturnErrorMsg(content, regExps, repoMetaData);
    }
}
