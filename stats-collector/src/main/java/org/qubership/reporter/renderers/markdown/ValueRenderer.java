package org.qubership.reporter.renderers.markdown;

import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;

import static org.qubership.reporter.inspectors.api.model.result.ResultSeverity.*;

public class ValueRenderer {
    // UTF codes can be found here: https://www.w3schools.com/charsets/ref_emoji.asp
    private static final String OK_PREFIX = "✅&nbsp;";
    private static final String ERROR_PREFIX = "❌&nbsp;";
    private static final String WARN_PREFIX = "❎&nbsp;";
    private static final String INFO_PREFIX = "";

    public String getMd(OneMetricResult metricValue) {
        String mdStr = metricValue.getRawValue();

        if (OK.equals(metricValue.getSeverity())) mdStr = OK_PREFIX + mdStr;
        if (ERROR.equals(metricValue.getSeverity())) mdStr = ERROR_PREFIX + mdStr;
        if (INFO.equals(metricValue.getSeverity())) mdStr = INFO_PREFIX + mdStr;
        if (WARN.equals(metricValue.getSeverity())) mdStr = WARN_PREFIX + mdStr;

        if (metricValue.getHttpReference() != null) {
            return "[" + mdStr + "](" +  metricValue.getHttpReference() + ")";
        }

        return mdStr;
    }
}
