package org.qubership.reporter.inspectors.impl.files.mfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.metric.MetricType;
import org.qubership.reporter.inspectors.impl.files.AbstractRequiredFileInspector;
import org.qubership.reporter.inspectors.impl.files.RequiredFileExpectations;

public class ReadmeRequiredFileContent extends AbstractRequiredFileInspector {

    @Override
    public Metric getMetric() {
        Metric metric = new Metric("/README", MetricType.PERSISTENT, "/README", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
        metric.setDescriptionRef(""); // todo
        return metric;
    }

    @Override
    protected RequiredFileExpectations getFileRequirements() {
        RequiredFileExpectations fReqs = new RequiredFileExpectations("README.md");
        fReqs.setMinFileSizeInBytes(512L);
        return fReqs;
    }
}
