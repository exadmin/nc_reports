package org.qubership.reporter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.qubership.reporter.model.TheReport;
import org.qubership.reporter.model.TheReportItem;
import org.qubership.reporter.model.meta.PrintableField;
import org.qubership.reporter.utils.DateUtils;
import org.qubership.reporter.utils.FileUtils;
import org.qubership.reporter.utils.TheLogger;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

                    TheReportItem reportItem = mapper.readValue(jsonFile, TheReportItem.class);
                    theReport.addReportItems(reportItem);
                }

            }
        }


        mapper.writeValue(new File(allReposRootDir + File.separator + "combined-report.json"), theReport);

        String mdContent = createMD(theReport);
        FileUtils.saveToFile(mdContent, allReposRootDir + File.separator + "combined-report.md");
    }

    private static String createMD(TheReport theReport) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("# Report on " + DateUtils.getCurrentDateTimeStamp() + "\n");

        // define table headers
        List<Field> annoFields = new ArrayList<>();

        Field[] fields = TheReportItem.class.getDeclaredFields();
        for (Field field : fields) {
            Annotation anPrintable = field.getAnnotation(PrintableField.class);
            if (anPrintable == null) continue;

            annoFields.add(field);
        }

        annoFields.sort((f1, f2) -> {
            PrintableField pColumn1 = f1.getAnnotation(PrintableField.class);
            PrintableField pColumn2 = f2.getAnnotation(PrintableField.class);

            // order columns by "order" value
            int result = pColumn1.order().compareTo(pColumn2.order());
            if (result != 0) return result;

            // in case "order" is not set or both columns have same order value - compare by visual-name
            return pColumn1.visualName().compareTo(pColumn2.visualName());
        });

        // print out table header
        for (Field field : annoFields) {
            sb.append("| ").append(field.getAnnotation(PrintableField.class).visualName()).append(" ");
        }
        sb.append("|\n");

        // print horizontal delimiter
        for (Field field : annoFields) {
            sb.append("|:----");
        }
        sb.append("|\n");

        // print out data
        for (TheReportItem item : theReport.getReportItems()) {
            for (Field field : annoFields) {
                Object value = getValue(item, field);
                sb.append("| ").append(value);
            }
            sb.append("|\n");
        }

        return sb.toString();
    }


    private static Object getValue(TheReportItem item, Field field) throws IllegalAccessException {
        field.setAccessible(true);
        return field.get(item);
    }
}
