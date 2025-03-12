package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.model.MetricGroup;

public class OneMetricResult {
    private final String rawValue;
    private final ResultSeverity severity;
    private final String metricName;
    private MetricGroup metricGroup;
    private String httpReference;
    private String toolTipForGithubOnly;
    private TextAlign textAlign;

    public OneMetricResult(String metricName, ResultSeverity severity, String rawValue) {
        this.rawValue = rawValue;
        this.severity = severity;
        this.metricName = metricName;
    }

    public String getRawValue() {
        return rawValue;
    }

    public ResultSeverity getSeverity() {
        return severity;
    }

    public String getMetricName() {
        return metricName;
    }

    public String getHttpReference() {
        return httpReference;
    }

    public void setHttpReference(String httpReference) {
        this.httpReference = httpReference;
    }

    public String getToolTipForGithubOnly() {
        return toolTipForGithubOnly;
    }

    public void setToolTipForGithubOnly(String toolTipForGithubOnly) {
        this.toolTipForGithubOnly = toolTipForGithubOnly;
    }

    public TextAlign getTextAlign() {
        if (textAlign == null) return TextAlign.LEFT_MIDDLE;
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public MetricGroup getMetricGroup() {
        return metricGroup;
    }

    public void setMetricGroup(MetricGroup metricGroup) {
        this.metricGroup = metricGroup;
    }
}
