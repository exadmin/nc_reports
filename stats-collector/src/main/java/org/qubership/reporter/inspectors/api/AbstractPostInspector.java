package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroup;
import org.qubership.reporter.inspectors.api.model.metric.MetricType;

import java.util.List;
import java.util.Map;

public abstract class AbstractPostInspector extends AbstractInspector {
    public abstract void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData);

    @Override
    protected Metric newMetric(String persistenceId, String visualName, MetricGroup group) {
        return new Metric(persistenceId, MetricType.AGGREGATIVE, visualName, group);
    }
}
