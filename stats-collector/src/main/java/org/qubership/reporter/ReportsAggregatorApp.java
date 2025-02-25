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
                File jsonFile = new File(nextFile + File.separator + ReposAnalyzerApp.REPORT_SHORT_FILE_NAME);
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

    // UTF codes can be found here: https://www.w3schools.com/charsets/ref_emoji.asp
    private static final String OK_PREFIX = "✅&nbsp;";
    private static final String ER_PREFIX = "❌&nbsp;";
    private static final String WARN_PREFIX = "❎&nbsp;";

    private static String createMD(TheReportModel theReport) throws Exception {
        theReport.normalizeData();

        StringBuilder sb = new StringBuilder();
        // add html content with references to styles.css
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<link rel=\"stylesheet\" href=\"css/styles.css\">\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("\n");



    // render report
        sb.append("# Report on " + DateUtils.getCurrentDateTimeStamp() + "\n");
        sb.append("\n");

        sb.append("<div class=\"table-wrapper\" markdown=\"block\">\n");
        sb.append("\n"); // note: empty line between div and markdown content is required

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

        sb.append("\n");
        sb.append("</div>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");

        return sb.toString();
    }

}
