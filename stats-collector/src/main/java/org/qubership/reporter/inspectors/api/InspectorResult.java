package org.qubership.reporter.inspectors.api;

public class InspectorResult {
    private final String message;
    private final MessageType msgType;
    private final String metricName;

    InspectorResult(String metricName, MessageType msgType, String message) {
        this.message = message;
        this.msgType = msgType;
        this.metricName = metricName;
    }

    public String getMessage() {
        return message;
    }

    public MessageType getMsgType() {
        return msgType;
    }

    public String getMetricName() {
        return metricName;
    }
}
