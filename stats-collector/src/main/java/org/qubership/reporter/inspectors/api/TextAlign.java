package org.qubership.reporter.inspectors.api;

public enum TextAlign {
    LEFT_MIDDLE("td-left-middle"),
    CENTER_MIDDLE("td-center-middle"),
    RIGHT_MIDDLE("td-right-middle");

    private String htmlClass;

    TextAlign(String htmlClass) {
        this.htmlClass = htmlClass;
    }

    @Override
    public String toString() {
        return htmlClass;
    }
}
