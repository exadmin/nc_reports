package org.qubership.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.model.TheReportModel;
import org.qubership.reporter.utils.DateUtils;
import org.qubership.reporter.utils.FileUtils;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.util.Map;

import static org.qubership.reporter.inspectors.api.MessageType.*;

public class ReportsAggregatorApp {
    public static void main(String[] args) throws Exception {
        final String allReposRootDir = args[0].trim();

        final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        TheReportModel theReport = new TheReportModel();

        File dir = new File(allReposRootDir);
        File[] files = dir.listFiles();
        for (File nextFile : files) {
            if (nextFile.isDirectory()) {
                File jsonFile = new File(nextFile.toString() + File.separator + ReposAnalyzerApp.REPORT_SHORT_FILE_NAME);
                if (jsonFile.isFile() && jsonFile.exists()) {
                    TheLogger.debug("Reading json-report at '" + jsonFile + "'");

                    Map<String, String> map = mapper.readValue(jsonFile, new TypeReference<>() {});
                    theReport.addData(map);
                }

            }
        }


        // mapper.writeValue(new File(allReposRootDir + File.separator + "combined-report.json"), theReport);
        // todo: save raw map

        String mdContent = createMD(theReport);
        FileUtils.saveToFile(mdContent, allReposRootDir + File.separator + "combined-report.md");
    }

    private static final String OK_PREFIX = "✅&nbsp;";
    private static final String ER_PREFIX = "❌&nbsp;";
    private static final String WARN_PREFIX = "⚡&nbsp;"; //&26a1;

    private static String createMD(TheReportModel theReport) throws Exception {
        theReport.normalizeData();

        StringBuilder sb = new StringBuilder();
        sb.append("# Report on " + DateUtils.getCurrentDateTimeStamp() + "\n");

        for (String colName : theReport.getColumnNames()) {
            sb.append("| ").append(colName).append(" ");
        }
        sb.append("|\n");

        // print horizontal delimiter
        for (String colName : theReport.getColumnNames()) {
            sb.append("|:----");
        }
        sb.append("|\n");

        for (String rowName : theReport.getRowNames()) {
            for (String colName : theReport.getColumnNames()) {
                String value = theReport.getValue(rowName, colName);
                if (value.startsWith(OK.toString())) value = value.replaceFirst(OK.toString(), OK_PREFIX);
                if (value.startsWith(ERROR.toString())) value = value.replaceFirst(ERROR.toString(), ER_PREFIX);
                if (value.startsWith(INFO.toString())) value = value.replaceFirst(INFO.toString(), "");
                if (value.startsWith(WARN.toString())) value = value.replaceFirst(WARN.toString(), WARN_PREFIX);

                sb.append("| ").append(value);
            }
            sb.append("|\n");
        }

        return sb.toString();
    }

}
