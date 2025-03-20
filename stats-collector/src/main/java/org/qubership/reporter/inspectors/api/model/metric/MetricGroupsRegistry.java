package org.qubership.reporter.inspectors.api.model.metric;

import java.util.ArrayList;
import java.util.List;

public class MetricGroupsRegistry {
    public static final MetricGroup SYSTEM_METRIC_GROUP = new MetricGroup("System", 0, true);

    public static final MetricGroup EXECUTIVE_SUMMARY = new MetricGroup("Summary", 1, false);

    public static final MetricGroup CODE_QUALITY_GROUP = new MetricGroup("Code Metrics", 2, false);

    public static final MetricGroup MANDATORY_FILES_GROUP = new MetricGroup("Mandatory Files", 3, false);

    public static final MetricGroup META_DATA_GROUP = new MetricGroup("Meta-data", 4, false);

    private static final List<MetricGroup> ALL_METRIC_GROUPS = new ArrayList<>();
    static {
        ALL_METRIC_GROUPS.add(SYSTEM_METRIC_GROUP);
        ALL_METRIC_GROUPS.add(EXECUTIVE_SUMMARY);
        ALL_METRIC_GROUPS.add(CODE_QUALITY_GROUP);
        ALL_METRIC_GROUPS.add(MANDATORY_FILES_GROUP);
        ALL_METRIC_GROUPS.add(META_DATA_GROUP);
    }

    public static List<MetricGroup> getAllMetricGroups() {
        return new ArrayList<>(ALL_METRIC_GROUPS);
    }
}
