package org.qubership.reporter.renderers.markdown;

import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.model.ReportModel;
import org.qubership.reporter.utils.DateUtils;
import org.qubership.reporter.utils.FileUtils;

public class MarkdownRenderer {
    private final ValueRenderer valueRenderer = new ValueRenderer();

    public void createMarkdawnFile(ReportModel report, String outputFileName) throws Exception {
        String mdContent = createMarkdawnString(report);

        FileUtils.saveToFile(mdContent, outputFileName);
    }

    private String createMarkdawnString(ReportModel theReport) throws Exception {

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

        for (String colName : theReport.getMetricNames()) {
            sb.append("| ").append(colName).append(" ");
        }
        sb.append("|\n");

        // print horizontal delimiter
        for (String colName : theReport.getMetricNames()) {
            sb.append("|:----");
        }
        sb.append("|\n");


        for (String rowName : theReport.getRepoNames()) {
            for (String colName : theReport.getMetricNames()) {
                OneMetricResult metricValue = theReport.getValue(rowName, colName);

                String mdStr = valueRenderer.getMd(metricValue);
                sb.append("| ").append(mdStr);
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
