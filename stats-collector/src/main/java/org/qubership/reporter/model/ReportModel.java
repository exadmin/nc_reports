package org.qubership.reporter.model;

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.qubership.reporter.inspectors.InspectorsHolder;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.inspectors.api.ResultSeverity;
import org.qubership.reporter.inspectors.api.files.AFileInspector;

import java.util.*;

public class ReportModel {
   // rowID -> Map<Key, Value>
   // private Map<String, Map<String, String>> data = new HashMap<>();
   private List<String> repoNames;
   private List<String> metricNames;
   private MultiKeyMap<String, OneMetricResult> multiMap = new MultiKeyMap<>(); // key1 & key2 are String, value is OneMetricResult

   public void addData(String repoName, Map<String, OneMetricResult> repoData) {
      repoNames = null;
      metricNames = null;

      for (Map.Entry<String, OneMetricResult> me : repoData.entrySet()) {
         multiMap.put(repoName, me.getKey(), me.getValue());
      }
   }

   /**
    * Fills columns and rows
    */
   private void prepareData() {
      // store all data into 2D table
      this.repoNames = new ArrayList<>();
      this.metricNames = new ArrayList<>();

      // collect metrics by registered inspectors
      for (ARepositoryInspector insp : InspectorsHolder.getRegisteredInspectors()) {
         String metricName = insp.getMetricName();
         if (!metricNames.contains(metricName)) {
            metricNames.add(metricName);
         } else {
            throw new IllegalStateException("Duplicate metric name is found between inspectors: " + metricName);
         }
      }

      for (Map.Entry<MultiKey<? extends String>, OneMetricResult> me : multiMap.entrySet()) {
         String repoName   = me.getKey().getKey(0); // repo
         // String metricName = me.getKey().getKey(1); // metric
         // OneMetricResult value = me.getValue();     // value

         if (!repoNames.contains(repoName)) {
            repoNames.add(repoName);
         }
      }

      // sort rows by ABC desc
      repoNames.sort(Comparator.naturalOrder());

      // now assign row-nums
      int rowNumber = 1; // virtual row
      for (String row : repoNames) {
         OneMetricResult omRes = new OneMetricResult(ReservedColumns.NUM, ResultSeverity.INFO, "" + rowNumber++);
         multiMap.put(row, ReservedColumns.NUM, omRes); // todo: analyze
      }

      // sort columns by ABC desc and set "ID" first
      // metricNames.sort(Comparator.naturalOrder());
      metricNames.remove(ReservedColumns.ID);
      metricNames.remove(ReservedColumns.NUM);
      metricNames.add(0, ReservedColumns.ID);
      metricNames.add(0, ReservedColumns.NUM);

      // make data unmodifiable
      repoNames = Collections.unmodifiableList(repoNames);
      metricNames = Collections.unmodifiableList(metricNames);
   }

   public OneMetricResult getValue(String row, String col) {
      return multiMap.get(row, col);
   }

   public List<String> getRepoNames() {
      if (repoNames == null) prepareData();
      return repoNames;
   }

   public List<String> getMetricNames() {
      if (metricNames == null) prepareData();
      return metricNames;
   }
}
