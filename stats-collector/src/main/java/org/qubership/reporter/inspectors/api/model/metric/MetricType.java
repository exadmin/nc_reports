package org.qubership.reporter.inspectors.api.model.metric;

public enum MetricType {
    // Metrics of PERSISTENT type will be saved into data-base
    PERSISTENT,

    // Metrics of NON_PERSISTENT type will not be saved into data-base, i.e. they can be calculated in runtime
    NON_PERSISTENT
}
