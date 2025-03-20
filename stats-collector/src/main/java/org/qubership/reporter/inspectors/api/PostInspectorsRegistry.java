package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.impl.postinstpectors.summary.ErrorsCountInspector;
import org.qubership.reporter.inspectors.impl.postinstpectors.summary.SortRepositoriesByABC;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PostInspectorsRegistry {
    private static final List<AbstractPostInspector> registeredInspectors = new ArrayList<>();
    static {
        registeredInspectors.add(new ErrorsCountInspector());
        registeredInspectors.add(new SortRepositoriesByABC());
    }

    public static List<AbstractPostInspector> getRegisteredInspectors() {
        return Collections.unmodifiableList(registeredInspectors);
    }

}
