package org.qubership.reporter.renderers.html;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.utils.StrUtils;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import static org.qubership.reporter.inspectors.api.model.result.ResultSeverity.*;
import static org.qubership.reporter.utils.StrUtils.notNull;

public class HtmlValueRenderer {
    // UTF codes can be found here: https://www.w3schools.com/charsets/ref_emoji.asp
    private static final String OK_PREFIX = "✅&nbsp;";
    private static final String ERROR_PREFIX = "❌&nbsp;";
    private static final String WARN_PREFIX = "⛅&nbsp;";
    private static final String INFO_PREFIX = "";
    private static final String SECURITY_PREFIX = "&#128561;&nbsp;";
    private static final String SKIP_PREFIX = "&#x1F4A4&nbsp;";

    public String getHtml(OneMetricResult metricValue, Metric metric) {
        String mdStr = escapeHtml4(metricValue.getRawValue());

        if (OK.equals(metricValue.getSeverity())) mdStr = OK_PREFIX + notNull(mdStr);
        if (ERROR.equals(metricValue.getSeverity())) mdStr = ERROR_PREFIX + notNull(mdStr);
        if (INFO.equals(metricValue.getSeverity())) mdStr = INFO_PREFIX + notNull(mdStr);
        if (WARN.equals(metricValue.getSeverity())) mdStr = WARN_PREFIX + notNull(mdStr);
        if (SECURITY_ISSUE.equals(metricValue.getSeverity())) mdStr = SECURITY_PREFIX + notNull(mdStr);
        if (SKIP.equals(metricValue.getSeverity())) mdStr = SKIP_PREFIX + notNull(mdStr);

        if (metricValue.getHttpReference() != null) {
            return "<a href=\"" + metricValue.getHttpReference() + "\">" + mdStr + "</a>";
        }

        return mdStr;
    }

    public String getTitle(String repoName, Metric metric, OneMetricResult metricValue) {
        String titleText = escapeHtml4(metricValue.getTitleText());
        if (StrUtils.isEmpty(titleText)) return "";

        repoName = escapeHtml4(repoName);
        String metricName = escapeHtml4(metric.getVisualName());


        return  "<p class='my-tooltip'>Metric    : <b>" + metricName + "</b><br>" +
                                             "Repository: <b>" + repoName + "</b><br>" +
                                             "<br>" +
                                             "Message   : <b>" + notNull(titleText) + "</b>" +
                                             "</p>";
    }
}
