package org.qubership.reporter.inspectors.api;

public class InspectorResult {
    private final String message;
    private final BinnaryResult okOrError;
    private final String metricName;

    InspectorResult(String metricName, BinnaryResult okOrError, String message) {
        this.message = message;
        this.okOrError = okOrError;
        this.metricName = metricName;
    }

    public String getMessage() {
        return message;
    }

    public BinnaryResult getOkOrError() {
        return okOrError;
    }

    public String getMetricName() {
        return metricName;
    }
}
