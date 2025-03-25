package org.qubership.reporter.inspectors.api.model;

public enum TextAlign {
    LEFT_MIDDLE("td-left-middle"),
    CENTER_MIDDLE("td-center-middle"),
    RIGHT_MIDDLE("td-right-middle");

    final private String htmlClass;

    TextAlign(String htmlClass) {
        this.htmlClass = htmlClass;
    }

    @Override
    public String toString() {
        return htmlClass;
    }
}
