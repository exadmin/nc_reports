package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.inspectors.api.model.TextAlign;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroup;

public abstract class AbstractInspector {
    /**
     * Returns metric definition which is analyzed by this inspector instance.
     * All results will be stored in the report under this metric.
     * @return Metric, can't be null
     */
    public abstract Metric getMetric();

    // Auxiliary methods below

    protected OneMetricResult createMetricResult(String msg, ResultSeverity severity, String httpRef, TextAlign textAlign) {
        OneMetricResult result = new OneMetricResult(getMetric(), severity, msg);
        result.setTextAlign(textAlign);
        result.setHttpReference(httpRef);
        return result;
    }

    protected Metric newMetric(String persistenceId, String visualName, MetricGroup group) {
        Metric metric = new Metric(persistenceId, visualName, group);
        return metric;
    }

    protected OneMetricResult error(String msg) {
        return error(msg, null);
    }

    protected OneMetricResult error(String msg, String httpRef) {
        return createMetricResult(msg, ResultSeverity.ERROR, httpRef, TextAlign.CENTER_MIDDLE);
    }

    protected OneMetricResult ok(String msg) {
        return createMetricResult(msg, ResultSeverity.OK, null, TextAlign.CENTER_MIDDLE);
    }

    protected OneMetricResult warn(String msg) {
        return warn(msg, null);
    }

    protected OneMetricResult warn(String msg, String httpRef) {
        return createMetricResult(msg, ResultSeverity.WARN, httpRef, TextAlign.CENTER_MIDDLE);
    }

    protected OneMetricResult info(String msg) {
        return info(msg, null);
    }

    protected OneMetricResult info(String msg, String httpRef) {
        return createMetricResult(msg, ResultSeverity.INFO, httpRef, TextAlign.LEFT_MIDDLE);
    }

    protected OneMetricResult secError(String msg) {
        return secError(msg, null);
    }

    protected OneMetricResult secError(String msg, String httpRef) {
        return createMetricResult(msg, ResultSeverity.SECURITY_ISSUE, httpRef, TextAlign.CENTER_MIDDLE);
    }
}
