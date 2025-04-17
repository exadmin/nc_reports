package org.qubership.reporter.inspectors.api.model.result;

public enum ResultSeverity {
    OK("OK"),
    ERROR("ERROR"),
    INFO("INFO"),
    WARN("WARN"),
    SECURITY_ISSUE("SECURITY"),
    SKIP("SKIP");

    private final String asString;

    ResultSeverity(String asString) {
        this.asString = asString;
    }

    @Override
    public String toString() {
        return asString;
    }
}
