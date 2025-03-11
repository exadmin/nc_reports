package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Defines base abstraction of inspection implementation.
 * Please, register any new implementation in InspectorsHolder class.
 */
public abstract class ARepositoryInspector {
    /**
     * Return metric name which is calculated by current inspector. It will be printed in corresponding table column header.
     * Do not use "ID" value - it is reserved, See ReservedColumns#ID field
     * @return String
     */
    public abstract String getMetricName();

    /**
     * Returns metric description to be show in the report
     * @return String description
     */
    protected abstract String getMetricDescriptionInMDFormat();

    /**
     * Inspecion implementation. Result is returned in the container of OneMetricResult.
     * @param pathToRepository path to cloned repository on the local storage
     * @param repoMetaData current repository meta-data returned by github.com
     * @param allReposMetaData all repositories meta-data returned by github.com
     * @return OneMetricResult instance
     * @throws Exception
     */
    protected abstract OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception;

    protected final OneMetricResult inspectRepoFolder(String pathToRepository, List<Map<String, Object>> allReposMetaData) throws Exception {
        File repoDir = new File(pathToRepository);
        String expCloneUrl = "https://github.com/Netcracker/" + repoDir.getName() + ".git";

        Map<String, Object> repoMetaData = null;
        for (Map<String, Object> next : allReposMetaData) {
            if (expCloneUrl.equals(next.get("clone_url"))) {
                repoMetaData = next;
                break;
            }
        }

        if (repoMetaData == null) {
            TheLogger.error("Can't find meta-data for the repo " + pathToRepository);
        }

        return inspectRepoFolder(pathToRepository, repoMetaData, allReposMetaData);
    }



    protected OneMetricResult error(String msg) {
        OneMetricResult result = new OneMetricResult(getMetricName(), ResultSeverity.ERROR, msg);
        result.setTextAlign(TextAlign.CENTER_MIDDLE);
        return result;
    }

    protected OneMetricResult ok(String msg) {
        return new OneMetricResult(getMetricName(), ResultSeverity.OK, msg);
    }

    protected OneMetricResult warn(String msg) {
        return new OneMetricResult(getMetricName(), ResultSeverity.WARN, msg);
    }

    protected OneMetricResult info(String msg) {
        return new OneMetricResult(getMetricName(), ResultSeverity.INFO, msg);
    }

    public final OneMetricResult runInspectionFor(String pathToRepository, List<Map<String, Object>> metaData) {
        try {
            return inspectRepoFolder(pathToRepository, metaData);
        } catch (Exception ex) {
            return error("Internal error: " + ex);
        }
    }
}
