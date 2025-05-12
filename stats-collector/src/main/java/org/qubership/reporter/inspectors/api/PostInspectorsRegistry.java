package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.impl.postinstpectors.codequality.SonarMetricValuesBulkFulfilling;
import org.qubership.reporter.inspectors.impl.postinstpectors.summary.DailyStatus;
import org.qubership.reporter.inspectors.impl.postinstpectors.summary.ErrorsCountInspector;
import org.qubership.reporter.inspectors.impl.postinstpectors.summary.ErrorsDeltaInspector;
import org.qubership.reporter.inspectors.impl.system.SortRepositoriesByABC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostInspectorsRegistry {
    private static final List<AbstractPostInspector> registeredInspectors = new ArrayList<>();
    static {
        registeredInspectors.add(new ErrorsCountInspector());
        registeredInspectors.add(new ErrorsDeltaInspector());
        registeredInspectors.add(new SortRepositoriesByABC());
       // registeredInspectors.add(new WeeklyStatus());
        registeredInspectors.add(new DailyStatus());
        registeredInspectors.add(new SonarMetricValuesBulkFulfilling());
    }

    public static List<AbstractPostInspector> getRegisteredInspectors() {
        return Collections.unmodifiableList(registeredInspectors);
    }

}
