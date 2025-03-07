package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.api.files.AFileInspector;
import org.qubership.reporter.inspectors.api.files.FileRequirements;

public class ReadmeFileContent extends AFileInspector {
    @Override
    protected FileRequirements getFileRequirements() {
        FileRequirements fReqs = new FileRequirements("README.md");
        fReqs.setExpectedMinFileSizeInBytes(512L);
        return fReqs;
    }

    @Override
    public String getMetricName() {
        return "/README";
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Checks if '/README.md' file exist in the repository and contains not less then 5 lines.";
    }
}
