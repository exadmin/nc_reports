package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.impl.files.AbstractRequiredFileInspector;
import org.qubership.reporter.inspectors.impl.files.RequiredFileExpectations;

import java.util.*;
import java.util.regex.Pattern;

public abstract class AbstractGithubWorkflowRequiredFileInspector extends AbstractRequiredFileInspector {

    @Override
    public final Metric getMetric() {
        return getMetricWithDescription();
    }

    protected abstract Metric getMetricWithDescription();

    protected abstract String getShortFileNamePlacedInGitHubWorkflowFolder();

    protected abstract void addExpectedSha256Sums(List<String> sha256CheckSums);

    protected abstract void addExpectedContentRegExps(List<String> regExps);

    protected void addRestrictedContentRegExps(Map<String, Integer> regExpMap) {
        regExpMap.put("\\bpull_request_target\\s*:", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected List<String> repositoryNamesToIgnore() {
        return Arrays.asList("qubership-envgene-template", "qubership-envgene-instance");
    }

    @Override
    protected final RequiredFileExpectations getFileRequirements() {
        RequiredFileExpectations fReqs = new RequiredFileExpectations(".github/workflows/" + getShortFileNamePlacedInGitHubWorkflowFolder());

        // add sha256 sums - if set
        List<String> expSha256Sums = new ArrayList<>();
        addExpectedSha256Sums(expSha256Sums);
        if (!expSha256Sums.isEmpty()) {
            for (String next : expSha256Sums) {
                fReqs.addExpectedSha256CheckSum(next);
            }
        }

        {
            List<String> lines = new ArrayList<>();
            addExpectedContentRegExps(lines);
            for (String next : lines) {
                fReqs.addExpectationByRegExp(next);
            }
        }

        {
            Map<String, Integer> regExpMap = new HashMap<>();
            addRestrictedContentRegExps(regExpMap);
            for (Map.Entry<String, Integer> me : regExpMap.entrySet()) {
                fReqs.addRestrictedContentRegExp(me.getKey(), me.getValue());
            }
        }

        return fReqs;
    }
}
