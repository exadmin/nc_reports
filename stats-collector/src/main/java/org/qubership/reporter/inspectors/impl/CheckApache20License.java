package org.qubership.reporter.inspectors.impl;

import org.qubership.reporter.inspectors.api.ARepositoryInspector;
import org.qubership.reporter.inspectors.api.InspectorResult;
import org.qubership.reporter.utils.FileUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class CheckApache20License extends ARepositoryInspector {

    @Override
    public InspectorResult inspectRepoFolder(String pathToRepository, List<Map<String, Object>> metaData) {
        File licenseFile = Paths.get(pathToRepository, "LICENSE").toFile();
        if (!licenseFile.exists()) return error("Not found");
        if (!licenseFile.isFile()) return error("Not found");

        // check sha256 sum
        try {
            String sha256 = FileUtils.getSHA256(licenseFile.toString());
            if (LICENSE_SHA256_BASE64_EXP_VALUE_v1.equals(sha256)) return ok("Apache 2.0");
            if (LICENSE_SHA256_BASE64_EXP_VALUE_v2.equals(sha256)) return ok("Apache 2.0");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return error("SHA-256 failed");
    }

    @Override
    protected String getMetricDescriptionInMDFormat() {
        return "Checks if '/LICENSE' file exist in repository with expected content (SHA-256 check-sum is used)";
    }

    @Override
    protected String getMetricName() {
        return "License";
    }

    private static final String LICENSE_SHA256_BASE64_EXP_VALUE_v1 = "z8d0m5b2O9McPEK1xHG/dWgUBT6EfBDz6wA0F7xSPTA=";
    private static final String LICENSE_SHA256_BASE64_EXP_VALUE_v2 = "xx0jnfkXJvxRnG63LTGOxlggYnIysveWIZ6H3PNdCrQ=";

}
