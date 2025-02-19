package org.qubership.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.model.TheReport;
import org.qubership.reporter.model.TheReportItemModel;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;

public class ReportsAggregatorApp {
    public static void main(String[] args) throws Exception {
        final String allReposRootDir = args[0].trim();

        TheReport theReport = new TheReport();

        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        File dir = new File(allReposRootDir);
        File[] files = dir.listFiles();
        for (File nextFile : files) {
            if (nextFile.isDirectory()) {
                File jsonFile = new File(nextFile.toString() + File.separator + ReposAnalyzerApp.REPORT_SHORT_FILE_NAME);
                if (jsonFile.isFile() && jsonFile.exists()) {
                    TheLogger.debug("Reading json-report at '" + jsonFile + "'");

                    TheReportItemModel reportItem = mapper.readValue(jsonFile, TheReportItemModel.class);
                    theReport.addReportItems(reportItem);
                }

            }
        }


        mapper.writeValue(new File(allReposRootDir + File.separator + "combined-report.json"), theReport);

        // find all files
        // combine
        // printout as MD file
    }


}
