package org.qubership.reporter.renderers.markdown;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
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
        sb.append("# Report on ").append(DateUtils.getCurrentDateTimeStamp()).append("\n");
        sb.append("\n");

        sb.append("<div class=\"table-wrapper\" markdown=\"block\">\n");
        sb.append("\n"); // note: empty line between div and markdown content is required

        for (Metric metric : theReport.getMetrics()) {
            sb.append("| ").append(metric.getVisualName()).append(" ");
        }
        sb.append("|\n");

        // print horizontal delimiter
        for (Metric metric : theReport.getMetrics()) {
            sb.append("|:----");
        }
        sb.append("|\n");


        for (String rowName : theReport.getRepositoryNames()) {
            for (Metric metric : theReport.getMetrics()) {
                OneMetricResult metricValue = theReport.getValue(rowName, metric.getPersistenceId());

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
