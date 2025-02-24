package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.impl.CheckApache20License;
import org.qubership.reporter.inspectors.impl.ReadmeFileContent;
import org.qubership.reporter.inspectors.impl.TopicAdded;
import org.qubership.reporter.inspectors.impl.workflowfiles.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InspectorsHolder {
    private static List<ARepositoryInspector> registeredInspectors = new ArrayList<>();
    static {
        // register all necessary inspectors here
        registeredInspectors.add(new CheckApache20License());
        registeredInspectors.add(new ReadmeFileContent());
        registeredInspectors.add(new TopicAdded());

        registeredInspectors.add(new AutomaticPRLabeler());
        registeredInspectors.add(new CDXGenFileInspector());
        registeredInspectors.add(new CLAFileInspector());
        registeredInspectors.add(new PRCollectCommitMessagesFileInspector());
        registeredInspectors.add(new PRConventionalCommitsFileInspector());
        registeredInspectors.add(new PRLintTitleFileInspector());
        registeredInspectors.add(new PrettierFileInspector());
        registeredInspectors.add(new ProfanityFilterFileInspector());
        registeredInspectors.add(new SuperLinterFileInspector());

        registeredInspectors = Collections.unmodifiableList(registeredInspectors);
    }

    public static List<ARepositoryInspector> getRegisteredInspectors() {
        return registeredInspectors;
    }
}
