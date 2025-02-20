package org.qubership.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorResult;
import org.qubership.reporter.inspectors.api.InspectorsHolder;
import org.qubership.reporter.model.ReservedColumns;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ReposAnalyzerApp {
    public static final String REPORT_SHORT_FILE_NAME = "stats-collector-report.json";

    private ObjectMapper mapper;
    private InspectorsHolder iHolder = new InspectorsHolder();

    public ReposAnalyzerApp() {
        mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static void main(String[] args) throws Exception {
        String allReposRootDir = args[0].trim();

        ReposAnalyzerApp theApp = new ReposAnalyzerApp();

        File dir = new File(allReposRootDir);
        File[] files = dir.listFiles();

        if (files == null) {
            TheLogger.error("No any files found at '" + allReposRootDir + "'");
            System.exit(-1);
        }

        for (File nextFile : files) {
            if (nextFile.isDirectory()) {
                theApp.processRepoDir(nextFile);
            }
        }
    }

    private void processRepoDir(File repoDir) throws Exception {
        TheLogger.debug("Processing repository at '" + repoDir + "'");

        Map<String, String> map = new HashMap<>();

        // provide ID value into map
        map.put(ReservedColumns.ID, repoDir.getName().toLowerCase());

        // perform all registered checks
        for (ARepositoryInspector inspector : InspectorsHolder.getRegisteredInspectors()) {
            InspectorResult result = inspector.runInspectionFor(repoDir.getAbsolutePath());
            map.put(result.getMetricName(), result.getOkOrError() + ":" + result.getMessage());
        }

        mapper.writeValue(new File(repoDir + File.separator + REPORT_SHORT_FILE_NAME), map);
    }
}
