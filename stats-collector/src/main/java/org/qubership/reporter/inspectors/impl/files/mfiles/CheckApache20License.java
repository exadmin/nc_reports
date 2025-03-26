package org.qubership.reporter.inspectors.impl.files.mfiles;

import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.impl.files.AbstractRequiredFileInspector;
import org.qubership.reporter.inspectors.impl.files.RequiredFileExpectations;

public class CheckApache20License extends AbstractRequiredFileInspector {
    private static final String LICENSE_SHA256_BASE64_EXP_VALUE_v1 = "KD6mzCmXoacNoASeCa35MXu2DKG1Enm2UZa4OmnhmWs="; // suites for most (if trim whitespaces)


    @Override
    public Metric getMetric() {
        return newMetric("/License","/License", MetricGroupsRegistry.MANDATORY_FILES_GROUP)
                .setDescriptionRef("https://www.apache.org/licenses/LICENSE-2.0.txt");
    }

    @Override
    protected RequiredFileExpectations getFileRequirements() {
        RequiredFileExpectations fReqs = new RequiredFileExpectations("LICENSE");
        fReqs.setAllowTrim(true);
        fReqs.addExpectedSha256CheckSum(LICENSE_SHA256_BASE64_EXP_VALUE_v1);

        return fReqs;
    }
}
