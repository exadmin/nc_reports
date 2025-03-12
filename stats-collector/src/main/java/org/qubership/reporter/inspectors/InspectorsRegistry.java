package org.qubership.reporter.inspectors;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.impl.codequality.CodeCoverageBySonar;
import org.qubership.reporter.inspectors.impl.codequality.JavaVersion;
import org.qubership.reporter.inspectors.impl.codequality.LastUpdatedTime;
import org.qubership.reporter.inspectors.impl.metadata.TopicAdded;
import org.qubership.reporter.inspectors.impl.mfiles.CheckApache20License;
import org.qubership.reporter.inspectors.impl.mfiles.ReadmeFileContent;
import org.qubership.reporter.inspectors.impl.mfiles.workflowfiles.*;
import org.qubership.reporter.inspectors.impl.summary.ErrorsCountInspector;
import org.qubership.reporter.inspectors.impl.system.RepoIDInspector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InspectorsRegistry {
    private static List<ARepositoryInspector> registeredInspectors = new ArrayList<>();
    static {
        // register all necessary inspectors here
        registeredInspectors.add(new LastUpdatedTime());
        registeredInspectors.add(new CodeCoverageBySonar());
        registeredInspectors.add(new CheckApache20License());
        registeredInspectors.add(new ReadmeFileContent());
        registeredInspectors.add(new AutomaticPRLabeler());
        registeredInspectors.add(new CDXGenFileInspector());
        registeredInspectors.add(new CLAFileInspector());
        registeredInspectors.add(new PRCollectCommitMessagesFileInspector());
        registeredInspectors.add(new PRConventionalCommitsFileInspector());
        registeredInspectors.add(new PRLintTitleFileInspector());
        registeredInspectors.add(new PrettierFileInspector());
        registeredInspectors.add(new ProfanityFilterFileInspector());
        registeredInspectors.add(new SuperLinterFileInspector());
        registeredInspectors.add(new RepoIDInspector());

        registeredInspectors.add(new JavaVersion());
        registeredInspectors.add(new ErrorsCountInspector());

        registeredInspectors.add(new TopicAdded()); // keep it last
        registeredInspectors = Collections.unmodifiableList(registeredInspectors);
    }

    public static List<ARepositoryInspector> getRegisteredInspectors() {
        return registeredInspectors;
    }
}
