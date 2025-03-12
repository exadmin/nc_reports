package org.qubership.reporter.html;

import org.qubership.reporter.inspectors.InspectorsRegistry;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.model.MetricGroup;
import org.qubership.reporter.model.ReportModel;
import org.qubership.reporter.model.ReservedColumns;
import org.qubership.reporter.utils.FileUtils;
import org.qubership.reporter.utils.StrUtils;

import java.util.*;

public class HtmlRenderer {
    private static final String MAGIC_COMMENT_START = "<!-- DO-NOT-TOUCH-THIS-COMMENT-START -->";
    private static final String MAGIC_COMMENT_END = "<!-- DO-NOT-TOUCH-THIS-COMMENT-END -->";

    private static final String MAGIC_JS_START = "<!-- JavaScript Start -->";
    private static final String MAGIC_JS_END = "<!-- JavaScript End -->";

    private static final HtmlValueRenderer htmlValueRenderer = new HtmlValueRenderer();

    public void createHtmlFile(ReportModel reportModel, String outputFileName) throws Exception {
        String template = FileUtils.readFile("./docs/template.html");
        String jsHtml = generateJsContent(reportModel);
        String bodyHtml = generateBodyHtml(reportModel);

        template = StrUtils.replaceContent(template, MAGIC_COMMENT_START, MAGIC_COMMENT_END, bodyHtml);
        template = StrUtils.replaceContent(template, MAGIC_JS_START, MAGIC_JS_END, jsHtml);

        FileUtils.saveToFile(template, outputFileName);
    }

    private String generateJsContent(ReportModel reportModel) {
        StringBuilder sb = new StringBuilder();

        List<MetricGroup> mGroups = getGroupNamesToRender(InspectorsRegistry.getRegisteredInspectors());


        int i = 1;
        sb.append("<script type=\"text/javascript\" charset=\"utf-8\">\n");
        {
            for (MetricGroup groupName : mGroups) {
                sb.append("$(document).ready(function() {\n" +
                        "            $('#table-" + i + "').dataTable({\n" +
                        "                pageLength: -1\n" +
                        "            });\n" +
                        "        } );\n");

                i++;
            }
        }

        sb.append("</script>\n");

        return sb.toString();
    }

    private String generateBodyHtml(ReportModel reportModel) {
        StringBuilder sb = new StringBuilder();

        // render tabs first
        // then render content inside tabs
        final List<MetricGroup> mGroups = getGroupNamesToRender(InspectorsRegistry.getRegisteredInspectors());

        sb.append("<div id=\"tabs\">\n");
        {
            sb.append("    <ul>\n");
            {
                int i = 1;
                for (MetricGroup group : mGroups) {
                    sb.append("        <li><a href=\"#tabs-").append(i).append("\">").append(group.getName()).append("</a></li>\n");
                    i++;
                }
            }
            sb.append("    </ul>\n");

            int i = 1;
            for (MetricGroup group : mGroups) {
                sb.append("<div id=\"tabs-").append(i).append("\">\n");
                String tableHtml = generateTableHtmlByGroup(reportModel, group, i);
                sb.append(tableHtml);
                sb.append("</div>\n");
                i++;
            }
        }
        sb.append("</div>\n");

        return sb.toString();
    }

    private String generateTableHtmlByGroup(ReportModel reportModel, MetricGroup group, int tableNum) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table id=\"table-").append(tableNum).append("\" class=\"display\" style=\"width:100%\">\n");

        // define metrics set which belongs to the requested group
        Set<String> uniqueSet = new HashSet<>();

        for (String repoName : reportModel.getRepoNames()) {
            for (String metricName : reportModel.getMetricNames()) {
                OneMetricResult metricResult = reportModel.getValue(repoName, metricName);

                if (metricResult.getMetricGroup().isSystem() || group.equals(metricResult.getMetricGroup())) {
                    uniqueSet.add(metricName);
                }
            }
        }

        // re-sort metric names - to have system at the very begining
        List<String> sortedMetricNames = new ArrayList<>(uniqueSet);
        sortedMetricNames.remove(ReservedColumns.ID);
        sortedMetricNames.remove(ReservedColumns.NUM);
        sortedMetricNames.add(0, ReservedColumns.ID);
        sortedMetricNames.add(0, ReservedColumns.NUM);

        // table headers generating start
        {
            sb.append("    <thead>\n");
            sb.append("    <tr>\n");
            for (String metricName : sortedMetricNames) {
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
                for (String colName : sortedMetricNames) {
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

    private List<MetricGroup> getGroupNamesToRender(final List<ARepositoryInspector> allInspectors) {
        Set<MetricGroup> uniqueSet = new HashSet<>();

        for (ARepositoryInspector inspector : allInspectors) {
            if (!inspector.getMetricGroup().isSystem()) {
                uniqueSet.add(inspector.getMetricGroup());
            }
        }

        List<MetricGroup> list = new ArrayList<>(uniqueSet);
        list.sort(Comparator.comparingInt(MetricGroup::getOrder));

        return list;
    }
}
