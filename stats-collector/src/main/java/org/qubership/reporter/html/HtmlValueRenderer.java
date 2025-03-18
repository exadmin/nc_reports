package org.qubership.reporter.html;

import org.qubership.reporter.inspectors.api.OneMetricResult;

import static org.qubership.reporter.inspectors.api.ResultSeverity.*;

public class HtmlValueRenderer {
    // UTF codes can be found here: https://www.w3schools.com/charsets/ref_emoji.asp
    private static final String OK_PREFIX = "✅&nbsp;";
    private static final String ERROR_PREFIX = "❌&nbsp;";
    private static final String WARN_PREFIX = "❎&nbsp;";
    private static final String INFO_PREFIX = "";

    public String getHtml(OneMetricResult metricValue, String metricName) {
        String mdStr = metricValue.getRawValue();

        if (OK.equals(metricValue.getSeverity())) mdStr = OK_PREFIX + mdStr;
        if (ERROR.equals(metricValue.getSeverity())) mdStr = ERROR_PREFIX + mdStr;
        if (INFO.equals(metricValue.getSeverity())) mdStr = INFO_PREFIX + mdStr;
        if (WARN.equals(metricValue.getSeverity())) mdStr = WARN_PREFIX + mdStr;

        if (metricValue.getHttpReference() != null) {
            return "<a href=\"" + metricValue.getHttpReference() + "\">" + mdStr + "</a>";
        }

        return mdStr;
    }
}
