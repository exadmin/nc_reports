package org.qubership.reporter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class TheReport {
    @JsonProperty("items")
    List<TheReportItemModel> reportItems;

    public void addReportItems(TheReportItemModel reportItem) {
        if (this.reportItems == null) {
            this.reportItems = new ArrayList<>();
        }

        reportItems.add(reportItem);
    }
}
