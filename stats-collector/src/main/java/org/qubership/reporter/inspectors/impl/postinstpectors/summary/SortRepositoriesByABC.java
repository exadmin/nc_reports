package org.qubership.reporter.inspectors.impl.postinstpectors.summary;

import org.qubership.reporter.inspectors.api.AbstractPostInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SortRepositoriesByABC extends AbstractPostInspector {

    @Override
    public Metric getMetric() {
        return newMetric("#", "#", MetricGroupsRegistry.SYSTEM_METRIC_GROUP)
                .setDescription("")
                .setRenderingOrderWeight(-200)
                .setRenderOnEachReportTab(true);
    }

    @Override
    public void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData) {

        // sort rows by ABC desc
        reportModel.getRepositoryNames().sort(Comparator.naturalOrder());

        // now assign row-nums
        int rowNumber = 1; // virtual row
        for (String row : reportModel.getRepositoryNames()) {
            reportModel.addData(row, info("" + rowNumber));

            rowNumber++;
        }
    }
}
