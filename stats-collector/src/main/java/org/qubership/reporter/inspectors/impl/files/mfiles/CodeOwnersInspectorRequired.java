package org.qubership.reporter.inspectors.impl.files.mfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.impl.files.AbstractRequiredFileInspector;
import org.qubership.reporter.inspectors.impl.files.RequiredFileExpectations;

public class CodeOwnersInspectorRequired extends AbstractRequiredFileInspector {
    @Override
    public Metric getMetric() {
        return newMetric("Code Owners", "Code Owners", MetricGroupsRegistry.MANDATORY_FILES_GROUP)
                .setDescriptionRef("https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-code-owners");
    }

    @Override
    protected RequiredFileExpectations getFileRequirements() {
        RequiredFileExpectations fReqs = new RequiredFileExpectations();
        fReqs.addOneOfFilePath(".github/CODEOWNERS");
        fReqs.addOneOfFilePath("CODEOWNERS");
        fReqs.addOneOfFilePath("docs/CODEOWNERS");

        return fReqs;
    }
}
