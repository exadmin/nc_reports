package org.qubership.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.inspectors.api.AbstractPostInspector;
import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorsRegistry;
import org.qubership.reporter.inspectors.api.PostInspectorsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RepositoriesAnalyzer {
    public static final String REPORT_SHORT_FILE_NAME = "stats-collector-report.json";

    public RepositoriesAnalyzer() {
    }

    /**
     * Analyse all repositories in the specified common folder and return result.
     *
     * @param allReposRootDir String common folder which contains subdirs with repositories
     * @return Map of RepositoryShortName -> Map<Metric, Value>
     * @throws Exception
     */
    public ReportModel analyzeAllIn(String allReposRootDir, Connection jdbcConnection) throws Exception {
        File dir = new File(allReposRootDir);
        File[] files = dir.listFiles();

        if (files == null) {
            TheLogger.error("No any files found at '" + allReposRootDir + "'");
            System.exit(-1);
        }

        // read meta-data from all_repos_pageX.json files
        List<Map<String, Object>> metaData = loadMetaData(allReposRootDir);

        ReportModel report = new ReportModel();

        // process each repository directory
        for (File nextDir : files) {
            if (nextDir.isDirectory()) {
                if ("data".equals(nextDir.getName()))
                    continue; // we use this folder to store prev-reports, see main.yml // todo: change it

                List<OneMetricResult> oneRepoData = collectAllMetricsForSingleRepository(nextDir, metaData);
                report.addData(nextDir.getName(), oneRepoData);
            }
        }

        // run post-inspectors for aggregative functions
        for (AbstractPostInspector postInspector : PostInspectorsRegistry.getRegisteredInspectors()) {
            postInspector.doPostInspection(report, metaData, jdbcConnection);
        }

        return report;
    }

    private List<OneMetricResult> collectAllMetricsForSingleRepository(File repoDir, List<Map<String, Object>> metaData) throws Exception {
        TheLogger.debug("Processing repository at '" + repoDir + "'");

        List<OneMetricResult> result = new ArrayList<>();

        // perform all registered checks
        for (AbstractRepositoryInspector inspector : InspectorsRegistry.getRegisteredInspectors()) {
            List<OneMetricResult> oneMetricResults = inspector.runInspectionFor(repoDir.getAbsolutePath(), metaData);

            result.addAll(oneMetricResults);
        }

        return result;
    }

    private List<Map<String, Object>> loadMetaData(String allReposRootDir) throws Exception {
        File dir = new File(allReposRootDir);
        File[] files = dir.listFiles();

        final List<Map<String, Object>> resultList = new ArrayList<>(320);

        if (files != null) {
            ObjectMapper mapper = new ObjectMapper(new JsonFactory());
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            TypeReference<List<Map<String, Object>>> type = new TypeReference<>() {};

            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    if (fileName.startsWith("all_repos_page") && fileName.endsWith(".json")) {
                        List<Map<String, Object>> fileData = mapper.readValue(file, type);

                        resultList.addAll(fileData);
                    }
                }
            }
        }

        if (resultList.isEmpty())
            throw new IllegalStateException("No files 'all_repos_pageX.json' where found. Terminating");

        return resultList;
    }
}
