package org.qubership.reporter.inspectors.api.model.metric;

import java.util.Objects;

public class Metric {
    private String persistenceId;
    private String visualName;
    private MetricGroup group;
    private MetricType type;
    private String descriptionRef;
    private boolean renderOnEachReportTab;

    // during rendering all metrics can be reordered by order-weight. If order weights are equals then visual-name is used.
    // bigger weight values go to the right side
    private int renderingOrderWeight = 0;

    /**
     * Creates new metric instance of PERSISTENT type
     * @param visualName
     * @param group
     */
    public Metric(String persistenceId, String visualName, MetricGroup group) {
        this.persistenceId = persistenceId;
        this.type = MetricType.PERSISTENT;
        this.visualName = visualName;
        this.group = group;
    }

    public Metric(String persistenceId, MetricType type, String visualName, MetricGroup group) {
        this.persistenceId = persistenceId;
        this.type = type;
        this.visualName = visualName;
        this.group = group;
    }

    public String getVisualName() {
        return visualName;
    }

    public MetricGroup getGroup() {
        return group;
    }

    public MetricType getType() {
        return type;
    }

    public String getPersistenceId() {
        return persistenceId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Metric metric = (Metric) o;
        return Objects.equals(visualName, metric.visualName) && Objects.equals(group, metric.group) && type == metric.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(visualName, group, type);
    }

    public String getDescriptionRef() {
        return descriptionRef;
    }

    public Metric setDescriptionRef(String descriptionRef) {
        this.descriptionRef = descriptionRef;
        return getThis();
    }

    protected Metric getThis() {
        return this;
    }

    public int getRenderingOrderWeight() {
        return renderingOrderWeight;
    }

    public Metric setRenderingOrderWeight(int renderingOrderWeight) {
        this.renderingOrderWeight = renderingOrderWeight;
        return getThis();
    }

    public boolean isRenderOnEachReportTab() {
        return renderOnEachReportTab;
    }

    /**
     * If true - then such metric will be rendered on each tab of the produced report
     * @param renderOnEachReportTab
     * @return
     */
    public Metric setRenderOnEachReportTab(boolean renderOnEachReportTab) {
        this.renderOnEachReportTab = renderOnEachReportTab;
        return getThis();
    }

    public Metric setType(MetricType type) {
        this.type = type;
        return getThis();
    }
}
