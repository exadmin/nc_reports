package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.api.model.TextAlign;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroup;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;

public abstract class AbstractInspector {
    /**
     * Returns metric definition which is analyzed by this inspector instance.
     * All results will be stored in the report under this metric.
     * @return Metric, can't be null
     */
    public abstract Metric getMetric();

    // Auxiliary methods below

    protected OneMetricResult createMetricResult(String msg, ResultSeverity severity, String httpRef, TextAlign textAlign, String titleText) {
        OneMetricResult result = new OneMetricResult(getMetric(), severity, msg);
        result.setTextAlign(textAlign);
        result.setHttpReference(httpRef);
        result.setTitleText(titleText);
        return result;
    }

    protected Metric newMetric(String persistenceId, String visualName, MetricGroup group) {
        return new Metric(persistenceId, visualName, group);
    }

    // ERROR AUX API
    protected OneMetricResult error(String msg) {
        return error(msg, null);
    }

    protected OneMetricResult error(String msg, String httpRef) {
        return createMetricResult(msg, ResultSeverity.ERROR, httpRef, TextAlign.CENTER_MIDDLE, null);
    }

    protected OneMetricResult error(String msg, String httpRef, String titleText) {
        return createMetricResult(msg, ResultSeverity.ERROR, httpRef, TextAlign.CENTER_MIDDLE, titleText);
    }

    // OK AUX API
    protected OneMetricResult ok(String msg) {
        return ok(msg, null, null);
    }

    protected OneMetricResult ok(String msg, String httpRef) {
        return ok(msg, httpRef, null);
    }

    protected OneMetricResult ok(String msg, String httpRef, String titleText) {
        return createMetricResult(msg, ResultSeverity.OK, httpRef, TextAlign.CENTER_MIDDLE, titleText);
    }

    // WARN AUX API
    protected OneMetricResult warn(String msg) {
        return warn(msg, null);
    }

    protected OneMetricResult warn(String msg, String httpRef) {
        return warn(msg, httpRef, null);
    }

    protected OneMetricResult warn(String msg, String httpRef, String titleText) {
        return createMetricResult(msg, ResultSeverity.WARN, httpRef, TextAlign.CENTER_MIDDLE, titleText);
    }

    // INFO AUX API
    protected OneMetricResult info(String msg) {
        return info(msg, null);
    }

    protected OneMetricResult info(String msg, String httpRef) {
        return info(msg, httpRef, null);
    }

    protected OneMetricResult info(String msg, String httpRef, String titleText) {
        return createMetricResult(msg, ResultSeverity.INFO, httpRef, TextAlign.CENTER_MIDDLE, titleText);
    }

    // Security Error AUX API

    protected OneMetricResult secError(String msg) {
        return secError(msg, null);
    }

    protected OneMetricResult secError(String msg, String httpRef) {
        return secError(msg, httpRef, null);
    }

    protected OneMetricResult secError(String msg, String httpRef, String titleText) {
        return createMetricResult(msg, ResultSeverity.SECURITY_ISSUE, httpRef, TextAlign.CENTER_MIDDLE, titleText);
    }

    // Skip marker
    protected OneMetricResult skip(String msg, String httpRef, String titleText) {
        return createMetricResult(msg, ResultSeverity.SKIP, httpRef, TextAlign.CENTER_MIDDLE, titleText);
    }

    protected OneMetricResult skip(String msg, String httpRef) {
        return skip(msg, httpRef, null);
    }

    protected OneMetricResult skip(String msg) {
        return skip(msg, null, null);
    }
}
