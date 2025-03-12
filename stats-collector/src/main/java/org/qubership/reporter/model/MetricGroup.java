package org.qubership.reporter.model;

import java.util.Objects;

public class MetricGroup {
    private String name;
    private int order;
    private boolean isSystem;

    public MetricGroup(String name, int order, boolean isSystem) {
        this.name = name;
        this.order = order;
        this.isSystem = isSystem;
    }

    public String getName() {
        return name;
    }

    public int getOrder() {
        return order;
    }

    public boolean isSystem() {
        return isSystem;
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
