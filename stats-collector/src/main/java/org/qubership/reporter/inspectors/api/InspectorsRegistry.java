package org.qubership.reporter.inspectors.api;

import org.qubership.reporter.inspectors.impl.codequality.CodeCoverageBySonar;
import org.qubership.reporter.inspectors.impl.codequality.JavaVersion;
import org.qubership.reporter.inspectors.impl.codequality.LastUpdatedTime;
import org.qubership.reporter.inspectors.impl.codequality.OpenedPRsInspector;
import org.qubership.reporter.inspectors.impl.files.mfiles.CheckApache20License;
import org.qubership.reporter.inspectors.impl.files.mfiles.CodeOwnersInspectorRequired;
import org.qubership.reporter.inspectors.impl.files.mfiles.ReadmeRequiredFileContent;
import org.qubership.reporter.inspectors.impl.files.mfiles.workflowfiles.*;
import org.qubership.reporter.inspectors.impl.postinstpectors.summary.ErrorsDeltaInspector;
import org.qubership.reporter.inspectors.impl.system.RepoIDInspector;
import org.qubership.reporter.inspectors.impl.system.TopicAdded;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InspectorsRegistry {
    private static List<AbstractRepositoryInspector> registeredInspectors = new ArrayList<>();
    static {
        // register all necessary inspectors here
        registeredInspectors.add(new LastUpdatedTime());
        registeredInspectors.add(new CodeCoverageBySonar());
        registeredInspectors.add(new CheckApache20License());
        registeredInspectors.add(new ReadmeRequiredFileContent());
        registeredInspectors.add(new AutomaticPRLabeler());
        // registeredInspectors.add(new CDXGenRequiredFileInspector()); // discussed with Roman P - excluded currently
        registeredInspectors.add(new CLARequiredFileInspector());
        registeredInspectors.add(new PRCollectCommitMessagesRequiredFileInspector());
        registeredInspectors.add(new PRConventionalCommitsRequiredFileInspector());
        registeredInspectors.add(new PRLintTitleRequiredFileInspector());
        registeredInspectors.add(new PrettierRequiredFileInspector());
        registeredInspectors.add(new ProfanityFilterRequiredFileInspector());
        registeredInspectors.add(new SuperLinterRequiredFileInspector());
        registeredInspectors.add(new RepoIDInspector());
        registeredInspectors.add(new CodeOwnersInspectorRequired());
        registeredInspectors.add(new JavaVersion());

        registeredInspectors.add(new TopicAdded());


        registeredInspectors.add(new OpenedPRsInspector());

        registeredInspectors = Collections.unmodifiableList(registeredInspectors);
    }

    public static List<AbstractRepositoryInspector> getRegisteredInspectors() {
        return registeredInspectors;
    }
}
