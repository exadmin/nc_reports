package org.qubership.reporter.inspectors.api.model.result;

import org.qubership.reporter.inspectors.api.model.TextAlign;
import org.qubership.reporter.inspectors.api.model.metric.Metric;

import java.util.HashMap;
import java.util.Map;

public class OneMetricResult {
    private final Metric metric;
    private String rawValue;
    private ResultSeverity severity;
    private String httpReference;
    private TextAlign textAlign;
    private Map<String, Object> extraData;
    private String titleText;
    private boolean skipEscaping = false;

    public OneMetricResult(Metric metric, ResultSeverity severity, String rawValue) {
        this.rawValue = rawValue;
        this.severity = severity;
        this.metric = metric;
    }

    protected OneMetricResult getThis() {
        return this;
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

    public OneMetricResult setHttpReference(String httpReference) {
        this.httpReference = httpReference;
        return getThis();
    }

    public TextAlign getTextAlign() {
        if (textAlign == null) return TextAlign.CENTER_MIDDLE;
        return textAlign;
    }

    public OneMetricResult setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
        return getThis();
    }

    public OneMetricResult setExtraData(String key, Object value) {
        if (extraData == null) extraData = new HashMap<>();
        extraData.put(key, value);
        return getThis();
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public String getTitleText() {
        return titleText;
    }

    public OneMetricResult setTitleText(String titleText) {
        this.titleText = titleText;
        return getThis();
    }

    public OneMetricResult setRawValue(String rawValue) {
        this.rawValue = rawValue;
        return getThis();
    }

    public OneMetricResult setSeverity(ResultSeverity severity) {
        this.severity = severity;
        return getThis();
    }

    public boolean isSkipEscaping() {
        return skipEscaping;
    }

    public OneMetricResult setSkipEscaping(boolean skipEscaping) {
        this.skipEscaping = skipEscaping;
        return getThis();
    }
}
