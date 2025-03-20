package org.qubership.reporter.inspectors.api.model.result;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.qubership.reporter.inspectors.api.model.metric.Metric;

import java.util.ArrayList;
import java.util.List;

public class ReportModel {
   @JsonIgnore
   private final List<String> repositoryNames = new ArrayList<>();

   @JsonIgnore
   private final List<Metric> metrics = new ArrayList<>();

   // Multimap where key1 & key2 are Strings, value is OneMetricResult
   // base inspectors register their results into this data-set
   private final MultiKeyMap<String, OneMetricResult> dataMap = new MultiKeyMap<>();

   /**
    * Adds to the report a new inspection result.
    * @param repositoryName string repository name (short name, no URL or file-paths), just name
    * @param allMetricsResults OneMetricResult collection for one repository
    */
   public void addData(String repositoryName, List<OneMetricResult> allMetricsResults) {
      for (OneMetricResult oneMetricResult : allMetricsResults) {
         addData(repositoryName, oneMetricResult);
      }
   }

   public void addData(String repositoryName, OneMetricResult oneMetricResult) {
      if (!repositoryNames.contains(repositoryName)) repositoryNames.add(repositoryName);

      Metric metric = oneMetricResult.getMetric();
      if (!metrics.contains(metric)) metrics.add(metric);

      dataMap.put(repositoryName, metric.getPersistenceId(), oneMetricResult);
   }

   public OneMetricResult getValue(String repositoryName, String metricName) {
      return dataMap.get(repositoryName, metricName);
   }

   public List<String> getRepositoryNames() {
      return repositoryNames;
   }

   public List<Metric> getMetrics() {
      return metrics;
   }

   public MultiKeyMap<String, OneMetricResult> getDataMap() {
      return dataMap;
   }
}
