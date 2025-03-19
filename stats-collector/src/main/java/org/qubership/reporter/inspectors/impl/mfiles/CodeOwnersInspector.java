package org.qubership.reporter.inspectors.impl.mfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.files.AFileInspector;
import org.qubership.reporter.inspectors.api.files.FileRequirements;
import org.qubership.reporter.model.MetricGroup;

public class CodeOwnersInspector extends AFileInspector {
    @Override
    protected FileRequirements getFileRequirements() {
        FileRequirements fReqs = new FileRequirements();
        fReqs.addOneOfFilePath(".github/CODEOWNERS");
        fReqs.addOneOfFilePath("CODEOWNERS");
        fReqs.addOneOfFilePath("docs/CODEOWNERS");

        return fReqs;
    }

    @Override
    public String getMetricName() {
        return "Code Owners";
    }

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Checks if file .github/CODEOWNERS exists in the repository";
    }
}
