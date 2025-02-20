package org.qubership.reporter.inspectors.api;

/**
 * Defines base abstraction of inspection implementation.
 * Please, register any new implementation in InspectorsHolder class.
 */
public abstract class ARepositoryInspector {
    protected abstract InspectorResult inspectRepoFolder(String pathToRepository);

    /**
     * Return metric name which is calculated by current inspector. It will be printed in corresponding table column header.
     * Do not use "ID" value - it is reserved, See ReservedColumns#ID field
     * @return String
     */
    protected abstract String getMetricName();

    protected InspectorResult createError(String msg) {
        return new InspectorResult(getMetricName(), BinnaryResult.ERROR, msg);
    }

    protected InspectorResult ok(String msg) {
        return new InspectorResult(getMetricName(), BinnaryResult.OK, msg);
    }

    public final InspectorResult runInspectionFor(String pathToRepository) {
        try {
            return inspectRepoFolder(pathToRepository);
        } catch (Exception ex) {
            return createError("Internal error: " + ex);
        }
    }
}
