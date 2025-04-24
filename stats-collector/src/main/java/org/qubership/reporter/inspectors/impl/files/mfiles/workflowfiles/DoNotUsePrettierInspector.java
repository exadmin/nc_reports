package org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles;

import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class DoNotUsePrettierInspector extends AbstractRepositoryInspector {
    @Override
    protected List<OneMetricResult> inspectRepoFolderWithManyMetrics(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        return List.of(inspectRepoFolder(pathToRepository, repoMetaData, allReposMetaData));
    }

    @Override
    public Metric getMetric() {
        return newMetric("WF/Prettier", "Do not use Prettier", MetricGroupsRegistry.MANDATORY_FILES_GROUP);
    }

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        // "c:\SVN\Netcracker repos\qubership-cloud-passport-cli\.github\workflows\prettier.yaml"
        Path path = Paths.get(pathToRepository, "/.github/workflows/prettier.yaml");
        File file = path.toFile();

        if (file.exists() && file.isFile()) {
            return error("", null, "Prettier is deprecated. Use SuperLinter instead.");
        }

        return ok("");
    }
}
