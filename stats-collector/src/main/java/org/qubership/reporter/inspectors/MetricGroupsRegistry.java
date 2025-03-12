package org.qubership.reporter.inspectors;

import org.qubership.reporter.model.MetricGroup;

public class MetricGroupsRegistry {
    public static final MetricGroup SYSTEM_METRIC_GROUP = new MetricGroup("System", 0, true);

    public static final MetricGroup CODE_QUALITY_GROUP = new MetricGroup("Code Quality", 1, false);

    public static final MetricGroup MANDATORY_FILES_GROUP = new MetricGroup("Mandatory Files", 2, false);
}
