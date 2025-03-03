package org.qubership.reporter.html;

import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.model.ReportModel;
import org.qubership.reporter.utils.FileUtils;

public class HtmlRenderer {
    private static final String MAGIC_COMMENT_START = "<!-- DO-NOT-TOUCH-THIS-COMMENT-START -->";
    private static final String MAGIC_COMMENT_END = "<!-- DO-NOT-TOUCH-THIS-COMMENT-END -->";
    private static final HtmlValueRenderer htmlValueRenderer = new HtmlValueRenderer();

    public void createHtmlFile(ReportModel reportModel, String outputFileName) throws Exception {
        String template = FileUtils.readFile("./docs/template.html");
        String tableStr = generateHtmlTable(reportModel);

        int cutStart = template.indexOf(MAGIC_COMMENT_START);
        int cutEnd   = template.indexOf(MAGIC_COMMENT_END) + MAGIC_COMMENT_END.length();

        StringBuilder sb = new StringBuilder();
        sb.append(template.substring(0, cutStart));
        sb.append("\n");
        sb.append(tableStr);
        sb.append("\n");
        sb.append(template.substring(cutEnd));

        FileUtils.saveToFile(sb.toString(), outputFileName);
    }

    private String generateHtmlTable(ReportModel reportModel) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table id=\"main-table\" class=\"display\" style=\"width:100%\">\n");


        // table headers generating start
        {
            sb.append("    <thead>\n");
            sb.append("    <tr>\n");
            for (String metricName : reportModel.getMetricNames()) {
                sb.append("        <th>").append(metricName).append("</th>\n");
            }
            sb.append("    </tr>\n");
            sb.append("    </thead>\n");
        }
        // table headers generating end

        // table data generating start
        {
            sb.append("    <tbody>\n");
            for (String rowName : reportModel.getRepoNames()) {
                sb.append("        <tr>\n");
                for (String colName : reportModel.getMetricNames()) {
                    OneMetricResult metricValue = reportModel.getValue(rowName, colName);

                    String cellInternalHtml = htmlValueRenderer.getHtml(metricValue);
                    sb.append("            <td class=\"").append(metricValue.getTextAlign()).append("\">");
                    sb.append(cellInternalHtml);
                    sb.append("</td>\n");
                }
                sb.append("        </tr>\n");
            }
            sb.append("    </tbody>\n");
        }
        // table data generating end

        sb.append("</table>\n");

        return sb.toString();
    }
}
