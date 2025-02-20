package org.qubership.reporter.inspectors.api;

public enum BinnaryResult {
    OK("OK"), ERROR("ERROR");

    private final String asString;

    BinnaryResult(String asString) {
        this.asString = asString;
    }

    @Override
    public String toString() {
        return asString;
    }
}
