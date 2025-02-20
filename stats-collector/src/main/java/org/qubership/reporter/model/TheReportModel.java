package org.qubership.reporter.model;

import org.apache.commons.collections4.map.MultiKeyMap;

import java.util.*;

public class TheReportModel {
   // rowID -> Map<Key, Value>
   private Map<String, Map<String, String>> data = new HashMap<>();
   private List<String> rows;
   private List<String> columns;
   private MultiKeyMap<String, String> multiMap;

   public void addData(Map<String, String> map) {
      // search "ID" field
      map = new HashMap<>(map); // new map is required due to modification
      String idValue = map.remove(ReservedColumns.ID);
      if (idValue == null) {
         throw new IllegalStateException("No ID field is found in the map. Internal error!");
      }

      data.put(idValue, map);
   }

   public void normalizeData() {
      // store all data into 2D table
      this.multiMap = new MultiKeyMap<>();
      this.rows = new ArrayList<>();
      this.columns = new ArrayList<>();



      for (Map.Entry<String, Map<String, String>> me : data.entrySet()) {
         String key1 = me.getKey();
         rows.add(key1); // this is ID, i.e. repo name
         multiMap.put(key1, ReservedColumns.ID, key1);

         Map<String, String> localMap = me.getValue();
         for (Map.Entry<String, String> localMe : localMap.entrySet()) {
            String key2 = localMe.getKey();
            String value = localMe.getValue();

            if (!columns.contains(key2)) columns.add(key2);

            multiMap.put(key1, key2, value);
         }
      }

      // sort rows by ABC desc
      rows.sort(Comparator.naturalOrder());
      // now assign row-nums
      int rowNumber = 1; // virtual row
      for (String row : rows) {
         multiMap.put(row, ReservedColumns.NUM, "" + rowNumber++);
      }

      // sort columns by ABC desc and set "ID" first
      columns.sort(Comparator.naturalOrder());
      columns.add(0, ReservedColumns.ID);
      columns.add(0, ReservedColumns.NUM);

      // make data unmodifiable
      rows = Collections.unmodifiableList(rows);
      columns = Collections.unmodifiableList(columns);
   }

   public String getValue(String row, String col) {
      return multiMap.get(row, col);
   }

   public List<String> getRowNames() {
      return rows;
   }

   public List<String> getColumnNames() {
      return columns;
   }
}
