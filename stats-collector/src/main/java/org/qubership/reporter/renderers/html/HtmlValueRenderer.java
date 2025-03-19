package org.qubership.reporter.renderers.html;

import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.utils.StrUtils;

import static org.qubership.reporter.inspectors.api.ResultSeverity.*;
import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

public class HtmlValueRenderer {
    // UTF codes can be found here: https://www.w3schools.com/charsets/ref_emoji.asp
    private static final String OK_PREFIX = "✅&nbsp;";
    private static final String ERROR_PREFIX = "❌&nbsp;";
    private static final String WARN_PREFIX = "☔&nbsp;";
    private static final String INFO_PREFIX = "";
    private static final String SECURITY_PREFIX = "&#128561;&nbsp;";

    public String getHtml(OneMetricResult metricValue, String metricName) {
        String mdStr = metricValue.getRawValue();

        if (OK.equals(metricValue.getSeverity())) mdStr = OK_PREFIX;
        if (ERROR.equals(metricValue.getSeverity())) mdStr = ERROR_PREFIX;
        if (INFO.equals(metricValue.getSeverity())) mdStr = INFO_PREFIX + mdStr;
        if (WARN.equals(metricValue.getSeverity())) mdStr = WARN_PREFIX;
        if (SECURITY_ISSUE.equals(metricValue.getSeverity())) mdStr = SECURITY_PREFIX;

        if (metricValue.getHttpReference() != null) {
            return "<a href=\"" + metricValue.getHttpReference() + "\">" + mdStr + "</a>";
        }

        return mdStr;
    }

    public String getTitle(String repoName, String metricName, OneMetricResult metricValue) {
        String rawValue = escapeHtml4(metricValue.getRawValue());
        if (StrUtils.isEmpty(rawValue)) return "";

        repoName = escapeHtml4(repoName);
        metricName = escapeHtml4(metricName);


        String title = "<p class='my-tooltip'>Metric    : <b>" + metricName + "</b><br>" +
                                             "Repository: <b>" + repoName + "</b><br>" +
                                             "<br>" +
                                             "Message   : <b>" + rawValue + "</b>" +
                                             "</p>";

        return title;
    }
}
