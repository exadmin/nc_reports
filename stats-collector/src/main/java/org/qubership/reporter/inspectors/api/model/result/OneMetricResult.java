package org.qubership.reporter.inspectors.api.model.result;

import org.qubership.reporter.inspectors.api.model.TextAlign;
import org.qubership.reporter.inspectors.api.model.metric.Metric;

import java.util.HashMap;
import java.util.Map;

public class OneMetricResult {
    private final Metric metric;
    private final String rawValue;
    private final ResultSeverity severity;
    private String httpReference;
    private TextAlign textAlign;
    private Map<String, Object> extraData;
    private String titleText;

    public OneMetricResult(Metric metric, ResultSeverity severity, String rawValue) {
        this.rawValue = rawValue;
        this.severity = severity;
        this.metric = metric;
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
        if (textAlign == null) return TextAlign.CENTER_MIDDLE;
        return textAlign;
    }

    public void setTextAlign(TextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public void setExtraData(String key, Object value) {
        if (extraData == null) extraData = new HashMap<>();
        extraData.put(key, value);
    }

    public Map<String, Object> getExtraData() {
        return extraData;
    }

    public String getTitleText() {
        return titleText;
    }

    public void setTitleText(String titleText) {
        this.titleText = titleText;
    }
}
