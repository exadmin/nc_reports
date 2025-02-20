package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.impl.CheckApache20License;
import org.qubership.reporter.inspectors.impl.ReadmeFileContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InspectorsHolder {
    private static List<ARepositoryInspector> registeredInspectors = new ArrayList<>();
    static {
        // register all necessary inspectors here
        registeredInspectors.add(new CheckApache20License());
        registeredInspectors.add(new ReadmeFileContent());

        registeredInspectors = Collections.unmodifiableList(registeredInspectors);
    }

    public static List<ARepositoryInspector> getRegisteredInspectors() {
        return registeredInspectors;
    }
}
