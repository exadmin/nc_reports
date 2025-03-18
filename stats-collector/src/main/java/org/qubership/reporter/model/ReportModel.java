package org.qubership.reporter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.map.MultiKeyMap;
import org.qubership.reporter.inspectors.InspectorsRegistry;
import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.OneMetricResult;
import org.qubership.reporter.inspectors.api.ResultSeverity;
import org.qubership.reporter.utils.MiscUtils;

import java.io.File;
import java.util.*;

public class ReportModel {
   // rowID -> Map<Key, Value>
   // private Map<String, Map<String, String>> data = new HashMap<>();
   @JsonIgnore
   private List<String> repoNames;

   @JsonIgnore
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
      for (ARepositoryInspector insp : InspectorsRegistry.getRegisteredInspectors()) {
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
         OneMetricResult omRes = new OneMetricResult(ReservedColumns.NUM, ResultSeverity.INFO, "" + rowNumber);
         omRes.setMetricGroup(MetricGroupsRegistry.SYSTEM_METRIC_GROUP);
         multiMap.put(row, ReservedColumns.NUM, omRes); // todo: analyze
         rowNumber++;
      }

      addTotalScoresColumn();

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

   // todo: refactor - place to generalized approach
   private void addTotalScoresColumn() {

      // define virtual column with executive results
      for (String row : repoNames) {
         int errCount = 0;
         for (String metricName : metricNames) {
            OneMetricResult omResult = getValue(row, metricName);
            ResultSeverity resultSeverity = omResult.getSeverity();
            if (resultSeverity.equals(ResultSeverity.ERROR) || resultSeverity.equals(ResultSeverity.WARN)) {
               errCount++;
            }
         }

         OneMetricResult omRes = new OneMetricResult(ReservedColumns.ERRORS_COUNT_THIS_WEEK, ResultSeverity.INFO, "" + errCount);
         omRes.setMetricGroup(MetricGroupsRegistry.EXECUTIVE_SUMMARY);
         multiMap.put(row, ReservedColumns.ERRORS_COUNT_THIS_WEEK, omRes);
      }
   }

   public void saveToFile(String fileName) throws Exception {
      Map<String, String> newMap = new HashMap<>();


      for (Map.Entry<MultiKey<? extends String>, OneMetricResult> me : multiMap.entrySet()) {
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
