package org.qubership.reporter.inspectors.api;

public enum MessageType {
    OK("OK:"), ERROR("ERROR:"), INFO("INFO:"), WARN("WARN:");

    private final String asString;

    MessageType(String asString) {
        this.asString = asString;
    }

    @Override
    public String toString() {
        return asString;
    }
}
