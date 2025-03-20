package org.qubership.reporter.renderers.html;

import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroup;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
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

        List<MetricGroup> mGroups = getGroupNamesToRender(reportModel);


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
        final List<MetricGroup> mGroups = getGroupNamesToRender(reportModel);

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

                String tableHtml = generateTableHtmlByGroups(reportModel, group, i);
                sb.append(tableHtml);
                sb.append("</div>\n");
                i++;
            }
        }
        sb.append("</div>\n");

        return sb.toString();
    }

    private String generateTableHtmlByGroups(ReportModel reportModel, MetricGroup group, int tableNum) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table id=\"table-").append(tableNum).append("\" class=\"display\" style=\"width:100%\">\n");

        // define metrics set which belongs to the requested group
        List<Metric> allMetrics = new ArrayList<>(reportModel.getMetrics());
        allMetrics.sort((m1, m2) -> {
            int result = m1.getRenderingOrderWeight() - m2.getRenderingOrderWeight();
            if (result == 0) {
                m1.getVisualName().compareTo(m2.getVisualName());
            }

            return result;
        });

        // table headers generating start
        {
            sb.append("    <thead>\n");
            sb.append("    <tr>\n");
            for (Metric metric : allMetrics) {
                if (group.equals(metric.getGroup()) || metric.isRenderOnEachReportTab()) {
                    sb.append("        <th>").append(metric.getVisualName()).append("</th>\n");
                }
            }
            sb.append("    </tr>\n");
            sb.append("    </thead>\n");
        }
        // table headers generating end

        // table data generating start
        {
            sb.append("    <tbody>\n");
            for (String repoName : reportModel.getRepositoryNames()) {
                sb.append("        <tr>\n");
                for (Metric metric : allMetrics) {
                    if (!group.equals(metric.getGroup()) && !metric.isRenderOnEachReportTab()) continue;

                    OneMetricResult metricValue = reportModel.getValue(repoName, metric.getPersistenceId());

                    String cellInternalHtml = htmlValueRenderer.getHtml(metricValue, metric);
                    String cellTitle = htmlValueRenderer.getTitle(repoName, metric, metricValue);

                    sb.append("            <td title=\"" + cellTitle + "\" class=\"").append(metricValue.getTextAlign()).append("\">");
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

    private List<MetricGroup> getGroupNamesToRender(ReportModel reportModel) {
        Set<MetricGroup> metricGroupsToRender = new HashSet<>();

        for (Metric metric : reportModel.getMetrics()) {
            MetricGroup metricGroup = metric.getGroup();
            if (!metricGroup.isDoNotProduceTabOnTheReport())
                metricGroupsToRender.add(metricGroup);
        }

        List<MetricGroup> list = new ArrayList<>(metricGroupsToRender);
        list.sort(Comparator.comparingInt(MetricGroup::getOrder));

        return list;
    }
}
