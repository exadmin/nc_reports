package org.qubership.reporter.inspectors.impl.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.files.AFileInspector;
import org.qubership.reporter.inspectors.api.files.FileRequirements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public abstract class AGithubWorkflowFileInspector extends AFileInspector {
    protected abstract String getShortFileNamePlacedInGitHubWorkflowFolder();

    protected abstract void addExpectedSha256Sums(List<String> sha256CheckSums);

    protected abstract void addExpectedContentRegExps(List<String> regExps);

    protected void addRestrictedContentRegExps(Map<String, Integer> regExpMap) {
        regExpMap.put("\\bpull_request_target\\s*:", Pattern.DOTALL + Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected final FileRequirements getFileRequirements() {
        FileRequirements fReqs = new FileRequirements(".github/workflows/" + getShortFileNamePlacedInGitHubWorkflowFolder());

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

    @Override
    protected final String getMetricDescriptionInMDFormat() {
        return "Checks if './github/workflows/" + getShortFileNamePlacedInGitHubWorkflowFolder() + "' file exists and have expected content";
    }


}
