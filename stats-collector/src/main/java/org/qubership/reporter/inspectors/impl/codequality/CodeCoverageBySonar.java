package org.qubership.reporter.inspectors.impl.codequality;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.utils.HttpUtils;
import org.qubership.reporter.utils.RepoUtils;
import org.qubership.reporter.utils.StrUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class CodeCoverageBySonar extends AbstractRepositoryInspector {


    @Override
    public Metric getMetric() {
        return newMetric("Sonar Code Coverage", "Code Coverage Status", MetricGroupsRegistry.CODE_QUALITY_GROUP)
                .setDescription("Returns coverage metric from Sonar Cloud service for the component");
    }

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        // define if sonar-call is registered in some github-action
        String prjKey = findSonarProjectKeyValue(pathToRepository, repoMetaData);
        if (StrUtils.isEmpty(prjKey)) {
            return error("Not registered");
        }

        // otherwise doing call to sonar-cloud
        OneMetricResult omResult = getMetricValueFromSonarCloud(prjKey, "coverage");
        if (!ResultSeverity.ERROR.equals(omResult.getSeverity())) {
            omResult.setHttpReference("https://sonarcloud.io/summary/overall?id=" + prjKey + "&branch=main");
        }

        return omResult;
    }

    private static final Pattern REGEXP_PRJ_KEY = Pattern.compile("\\bDsonar\\.projectKey\\s*=\\s*([^\\s]+)\\b", Pattern.CASE_INSENSITIVE);

    private static String findSonarProjectKeyValue(String pathToRepository, Map<String, Object> repoMetaData) {
        return "Netcracker_" + RepoUtils.getRepositoryName(repoMetaData); // very simple approach
    }

    private OneMetricResult getMetricValueFromSonarCloud(String sonarComponentName, String metricsCommaSeparated) {
        String url = "https://sonarcloud.io/api/measures/component?component="+ sonarComponentName + "&metricKeys=" + metricsCommaSeparated;
        return HttpUtils.doGet(url, new HttpUtils.IResponseHandler<>() {
            @Override
            protected OneMetricResult onSuccess(CloseableHttpResponse httpResponse, String responseBody) throws Exception {
                ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                TypeReference<Map<String, Object>> type = new TypeReference<>() {};

                Map<String, Object> data = mapper.readValue(responseBody, type);

                Map<String, Object> componentData = (Map<String, Object>) data.get("component");
                if (componentData == null) return createError("Error: parsing response. Component is absent.");

                List measuresData = (List) componentData.get("measures");
                if (measuresData == null) return createError("Error: parsing response. Measures are absent.");

                for (Object next : measuresData) {
                    Map<String, Object> measure = (Map<String, Object>) next;
                    String key = (String) measure.get("metric");
                    if ("coverage".equals(key)) {
                        String value = (String) measure.get("value");
                        if (StrUtils.isEmpty(value))
                            return createError("Error: parsing response. No 'value' for coverage metric");

                        // todo: select severity based on the value rate
                        OneMetricResult omResult = new OneMetricResult(getMetric(), ResultSeverity.OK, value + "%");
                        return omResult;
                    }
                }

                return createError("Error: No 'coverage' metric is returned in response.");
            }

            @Override
            protected OneMetricResult onError(int statusCode, CloseableHttpResponse httpResponse) throws Exception {
                return createError("Error: HTTP_CODE = " + statusCode);
            }

            @Override
            protected OneMetricResult onException(Exception ex) {
                return createError("Error: " + ex.toString());
            }

            private OneMetricResult createError(String titleText) {
                OneMetricResult omResult = new OneMetricResult(getMetric(), ResultSeverity.ERROR, null);
                omResult.setTitleText(titleText);
                return omResult;
            }
        });
    }
}
