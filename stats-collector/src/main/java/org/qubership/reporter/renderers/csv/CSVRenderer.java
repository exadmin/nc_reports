package org.qubership.reporter.renderers.csv;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.utils.FileUtils;

import java.util.*;

public class CSVRenderer {
    /**
     * Allows to create CSV-file with "REPO NAME, TOPICS" data
     * @param fileName
     */
    public void saveToCSVFile(ReportModel reportModel, String fileName) {
        try {

            Map<String, String> repo2Topic = new HashMap<>();

            List<String> repoNames = new ArrayList<>();
            for (Map.Entry<MultiKey<? extends String>, OneMetricResult> me : reportModel.getDataMap().entrySet()) {
                String name = me.getKey().getKey(0); // repo
                String metricName = me.getKey().getKey(1); // metric
                String rawValue   = me.getValue().getRawValue();     // value

                if ("Topics".equals(metricName)) {
                    rawValue = rawValue.replaceAll("<br>", " ");
                    repo2Topic.put(name, rawValue);
                }

                if (!repoNames.contains(name)) repoNames.add(name);
            }

            Collections.sort(repoNames);
            StringBuilder sb = new StringBuilder();
            sb.append("Repository, Topics\n");

            for (String name : repoNames) {
                sb.append(name).append(",").append(repo2Topic.get(name)).append("\n");
            }

            FileUtils.saveToFile(sb.toString(), fileName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
