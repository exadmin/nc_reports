package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.utils.RepoUtils;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Defines base abstraction of inspection implementation.
 * Please, register any new implementation in InspectorsHolder class.
 */
public abstract class AbstractRepositoryInspector extends AbstractInspector {
    /**
     * Inspecion implementation. Result is returned in the container of OneMetricResult.
     *
     * @param pathToRepository path to cloned repository on the local storage
     * @param repoMetaData     current repository meta-data returned by gitHub.com
     * @param allReposMetaData all repositories meta-data returned by gitHub.com
     * @return OneMetricResult instance
     * @throws Exception
     */
    protected abstract OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception;

    protected abstract List<OneMetricResult> inspectRepoFolderWithManyMetrics(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception;

    /**
     * Returns list of short repository names (i.e. just name, for instance "qubership-example") which must be ignored by the Inspector.
     * @return List of Strings
     */
    protected List<String> repositoryNamesToIgnore() {
        return Collections.emptyList();
    }

    protected final List<OneMetricResult> inspectRepoFolder(String pathToRepository, List<Map<String, Object>> allReposMetaData) throws Exception {
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

        // check if repository in the list of ignored
        String repoShortName = RepoUtils.getRepositoryName(repoMetaData);
        if (repositoryNamesToIgnore() != null && repositoryNamesToIgnore().contains(repoShortName)) {
            return Collections.singletonList(skip(""));
        }

        return inspectRepoFolderWithManyMetrics(pathToRepository, repoMetaData, allReposMetaData);
    }

    public final List<OneMetricResult> runInspectionFor(String pathToRepository, List<Map<String, Object>> metaData) {
        try {
            return inspectRepoFolder(pathToRepository, metaData);
        } catch (Exception ex) {
            OneMetricResult omResult = error("Internal error: " + ex + ", " +stackTraceToString(ex));
            return Collections.singletonList(omResult);
        }
    }

    public static String stackTraceToString(Throwable throwable) {
        return Arrays.stream(throwable.getStackTrace())
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n"));
    }
}
