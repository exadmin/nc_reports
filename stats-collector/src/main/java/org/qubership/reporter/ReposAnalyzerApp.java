package org.qubership.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.model.TheReportItem;
import org.qubership.reporter.utils.DateUtils;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;

public class ReposAnalyzerApp {
    public static final String REPORT_SHORT_FILE_NAME = "stats-collector-report.json";

    private ObjectMapper mapper;

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

        TheReportItem reportModel = new TheReportItem();
        reportModel.setName(repoDir.getName());
        reportModel.setVerifyResult(DateUtils.getCurrentDateTimeStamp());

        mapper.writeValue(new File(repoDir + File.separator + REPORT_SHORT_FILE_NAME), reportModel);
    }
}
