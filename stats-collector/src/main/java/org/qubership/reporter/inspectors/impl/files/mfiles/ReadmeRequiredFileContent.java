package org.qubership.reporter.inspectors.impl.files.mfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.impl.files.AbstractRequiredFileInspector;
import org.qubership.reporter.inspectors.impl.files.RequiredFileExpectations;
import org.qubership.reporter.inspectors.api.model.metric.MetricType;

public class ReadmeRequiredFileContent extends AbstractRequiredFileInspector {

    @Override
    public Metric getMetric() {
        Metric metric = new Metric("/README", MetricType.NORMAL, "/README", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
        metric.setDescription("Checks if '/README.md' file exist in the repository and contains not less then 5 lines.");
        return metric;
    }

    @Override
    protected RequiredFileExpectations getFileRequirements() {
        RequiredFileExpectations fReqs = new RequiredFileExpectations("README.md");
        fReqs.setMinFileSizeInBytes(512L);
        return fReqs;
    }
}
