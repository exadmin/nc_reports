package org.qubership.reporter.inspectors.api.model.metric;

import java.util.Objects;

public class MetricGroup {
    private String name;
    private int order;
    private boolean doNotProduceTabOnTheReport;

    public MetricGroup(String name, int order, boolean doNotProduceTabOnTheReport) {
        this.name = name;
        this.order = order;
        this.doNotProduceTabOnTheReport = doNotProduceTabOnTheReport;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public boolean isDoNotProduceTabOnTheReport() {
        return doNotProduceTabOnTheReport;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MetricGroup group = (MetricGroup) o;
        return Objects.equals(name, group.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
