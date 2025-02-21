package org.qubership.reporter.inspectors.api;

import java.util.List;
import java.util.Map;

/**
 * Defines base abstraction of inspection implementation.
 * Please, register any new implementation in InspectorsHolder class.
 */
public abstract class ARepositoryInspector {
    protected abstract InspectorResult inspectRepoFolder(String pathToRepository, List<Map<String, Object>> metaData) throws Exception;

    /**
     * Return metric name which is calculated by current inspector. It will be printed in corresponding table column header.
     * Do not use "ID" value - it is reserved, See ReservedColumns#ID field
     * @return String
     */
    protected abstract String getMetricName();

    protected abstract String getMetricDescriptionInMDFormat();

    protected InspectorResult error(String msg) {
        return new InspectorResult(getMetricName(), MessageType.ERROR, msg);
    }

    protected InspectorResult ok(String msg) {
        return new InspectorResult(getMetricName(), MessageType.OK, msg);
    }

    protected InspectorResult info(String msg) {
        return new InspectorResult(getMetricName(), MessageType.INFO, msg);
    }

    public final InspectorResult runInspectionFor(String pathToRepository, List<Map<String, Object>> metaData) {
        try {
            return inspectRepoFolder(pathToRepository, metaData);
        } catch (Exception ex) {
            return error("Internal error: " + ex);
        }
    }
}
