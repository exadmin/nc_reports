package org.qubership.reporter.renderers.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.utils.MiscUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class JsonRenderer {
    public void saveToFile(ReportModel report, String fileName) throws Exception {
        Map<String, String> newMap = new HashMap<>();

        for (Map.Entry<MultiKey<? extends String>, OneMetricResult> me : report.getDataMap().entrySet()) {
            String repoName   = me.getKey().getKey(0); // repo
            String metricName = me.getKey().getKey(1); // metric
            String rawValue   = me.getValue().getRawValue();     // value

            String newFatKey = MiscUtils.createComplexKey(repoName, metricName);
            newMap.put(newFatKey, rawValue);
        }

        ObjectMapper mapper = new ObjectMapper(new JsonFactory());
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.writeValue(new File(fileName), newMap);
    }
}
