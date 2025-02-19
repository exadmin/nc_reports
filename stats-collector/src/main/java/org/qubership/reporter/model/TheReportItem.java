package org.qubership.reporter.model;

import org.qubership.reporter.model.meta.PrintableField;

public class TheReportItem {
    @PrintableField(visualName = "Repository name", order = "1")
    private String name;

    @PrintableField(visualName = "Verification date", order = "2")
    private String verifyResult;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerifyResult() {
        return verifyResult;
    }

    public void setVerifyResult(String verifyResult) {
        this.verifyResult = verifyResult;
    }
}
