package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.api.files.AFileInspector;
import org.qubership.reporter.inspectors.api.files.FileRequirements;

public class CheckApache20License extends AFileInspector {
    private static final String LICENSE_SHA256_BASE64_EXP_VALUE_v1 = "z8d0m5b2O9McPEK1xHG/dWgUBT6EfBDz6wA0F7xSPTA=";
    private static final String LICENSE_SHA256_BASE64_EXP_VALUE_v2 = "xx0jnfkXJvxRnG63LTGOxlggYnIysveWIZ6H3PNdCrQ=";

    @Override
    protected FileRequirements getFileRequirements() {
        FileRequirements fReqs = new FileRequirements("LICENSE");
        fReqs.addExpectedSha256CheckSum(LICENSE_SHA256_BASE64_EXP_VALUE_v1);
        fReqs.addExpectedSha256CheckSum(LICENSE_SHA256_BASE64_EXP_VALUE_v2);

        return fReqs;
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Checks if '/LICENSE' file exist in repository with expected content (SHA-256 check-sum is used)";
    }

    @Override
    public String getMetricName() {
        return "/License";
    }



}
