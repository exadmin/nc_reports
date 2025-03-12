package org.qubership.reporter.inspectors;

import org.qubership.reporter.model.MetricGroup;

public class MetricGroupsRegistry {
    public static final MetricGroup SYSTEM_METRIC_GROUP = new MetricGroup("System", 0, true);

    public static final MetricGroup EXECUTIVE_SUMMARY = new MetricGroup("Summary", 1, false);

    public static final MetricGroup CODE_QUALITY_GROUP = new MetricGroup("Code Metrics", 2, false);

    public static final MetricGroup MANDATORY_FILES_GROUP = new MetricGroup("Mandatory Files", 3, false);

    public static final MetricGroup META_DATA_GROUP = new MetricGroup("Meta-data", 4, false);
}
