package org.qubership.reporter.inspectors.api;

public class OneMetricResult {
    private final String rawValue;
    private final ResultSeverity severity;
    private final String metricName;
    private String httpReference;

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
}
