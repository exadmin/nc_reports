package org.qubership.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorResult;
import org.qubership.reporter.inspectors.api.InspectorsHolder;
import org.qubership.reporter.model.ReservedColumns;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

        // read meta-data from all_repos_pageX.json files
        List<Map<String, Object>> metaData = theApp.loadMetaData(allReposRootDir);

        // process each repo
        for (File nextFile : files) {
            if (nextFile.isDirectory()) {
                theApp.processRepoDir(nextFile, metaData);
            }
        }
    }

    private void processRepoDir(File repoDir, List<Map<String, Object>> metaData) throws Exception {
        TheLogger.debug("Processing repository at '" + repoDir + "'");

        Map<String, String> map = new HashMap<>();

        // provide ID value into map
        map.put(ReservedColumns.ID, repoDir.getName().toLowerCase());

        // perform all registered checks
        for (ARepositoryInspector inspector : InspectorsHolder.getRegisteredInspectors()) {
            InspectorResult result = inspector.runInspectionFor(repoDir.getAbsolutePath(), metaData);
            map.put(result.getMetricName(), result.getMsgType() + result.getMessage());
        }

        mapper.writeValue(new File(repoDir + File.separator + REPORT_SHORT_FILE_NAME), map);
    }

    private List<Map<String, Object>> loadMetaData(String allReposRootDir) throws Exception {
        File dir = new File(allReposRootDir);
        File[] files = dir.listFiles();

        TypeReference<List<Map<String, Object>>> type = new TypeReference<>() {};
        List<Map<String, Object>> resultList = new ArrayList<>(320);

        for (File file : files) {
            if (file.isFile()) {
                String fileName = file.getName();
                if (fileName.startsWith("all_repos_page") && fileName.endsWith(".json")) {
                    List<Map<String, Object>> fileData = mapper.readValue(file, type);

                    resultList.addAll(fileData);
                }
            }
        }

        if (resultList.isEmpty()) throw new IllegalStateException("No files 'all_repos_pageX.json' where found. Terminating");

        return resultList;
    }
}
