package org.qubership.reporter.inspectors.api.model.result;

import org.qubership.reporter.inspectors.api.model.TextAlign;
import org.qubership.reporter.inspectors.api.model.metric.Metric;

public class OneMetricResult {
    private final Metric metric;
    private final String rawValue;
    private final ResultSeverity severity;
    private String httpReference;
    private TextAlign textAlign;

    public OneMetricResult(Metric metric, ResultSeverity severity, String rawValue) {
        this.rawValue = rawValue;
        this.severity = severity;
        this.metric = metric;

        // let's align all non-info results by center_middle by default
        if (!severity.equals(ResultSeverity.INFO)) textAlign = TextAlign.CENTER_MIDDLE;
    }

    public String getRawValue() {
        return rawValue;
    }

    public ResultSeverity getSeverity() {
        return severity;
    }

    public Metric getMetric() {
        return metric;
    }

    public String getHttpReference() {
        return httpReference;
    }

    public void setHttpReference(String httpReference) {
        this.httpReference = httpReference;
    }

    public TextAlign getTextAlign() {
        if (textAlign == null) return TextAlign.LEFT_MIDDLE;
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }
}
