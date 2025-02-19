package org.qubership.reporter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TheReport {
    @JsonProperty("items")
    List<TheReportItem> reportItems;

    public void addReportItems(TheReportItem reportItem) {
        if (this.reportItems == null) {
            this.reportItems = new ArrayList<>();
        }

        reportItems.add(reportItem);
    }

    public List<TheReportItem> getReportItems() {
        return reportItems;
    }
}
