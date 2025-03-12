package org.qubership.reporter.inspectors.impl.mfiles;

import org.qubership.reporter.inspectors.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.files.AFileInspector;
import org.qubership.reporter.inspectors.api.files.FileRequirements;
import org.qubership.reporter.model.MetricGroup;

public class CheckApache20License extends AFileInspector {
    private static final String LICENSE_SHA256_BASE64_EXP_VALUE_v1 = "KD6mzCmXoacNoASeCa35MXu2DKG1Enm2UZa4OmnhmWs="; // suites for most (if trim whitespaces)
    // private static final String LICENSE_SHA256_BASE64_EXP_VALUE_v1 = "bcDgaNzzpbyOBUIFuFt3IOHUkmW7xkv1FdLPeRl99po="; // for k8s-conformance\LICENSE where braces are wrong

    @Override
    protected FileRequirements getFileRequirements() {
        FileRequirements fReqs = new FileRequirements("LICENSE");
        fReqs.setAllowTrim(true);
        fReqs.addExpectedSha256CheckSum(LICENSE_SHA256_BASE64_EXP_VALUE_v1);

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

    @Override
    public MetricGroup getMetricGroup() {
        return MetricGroupsRegistry.MANDATORY_FILES_GROUP;
    }
}
