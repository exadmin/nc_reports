package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.api.files.AFileInspector;
import org.qubership.reporter.inspectors.api.files.FileRequirements;

public class AutomaticPRLabeler extends AFileInspector {
    @Override
    protected FileRequirements getFileRequirements() {
        FileRequirements req = new FileRequirements("./.github/workflows/automatic-pr-labeler.yaml");
        req.addExpectedSha256CheckSum("LXd62wzKhH05wia6N7v4f4Use5q1Uwk0QLDABoyXMLg=");

        return req;
    }

    @Override
    protected String getMetricName() {
        return "Automatic PR Labeler";
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Checks existence of ./.github/workflows/automatic-pr-labeler.yaml";
    }
}
