package org.qubership.reporter.inspectors.api;

public enum ResultSeverity {
    OK("OK:"), ERROR("ERROR:"), INFO("INFO:"), WARN("WARN:");

    private final String asString;

    ResultSeverity(String asString) {
        this.asString = asString;
    }

    @Override
    public String toString() {
        return asString;
    }
}
