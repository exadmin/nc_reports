package org.qubership.reporter.inspectors.impl.postinstpectors.codequality;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.qubership.reporter.inspectors.api.AbstractPostInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ReportModel;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.inspectors.impl.cncf.model.ModelsContainer;
import org.qubership.reporter.inspectors.impl.codequality.CodeCoverageBySonar;
import org.qubership.reporter.utils.HttpUtils;
import org.qubership.reporter.utils.MiscUtils;
import org.qubership.reporter.utils.TheLogger;
import org.qubership.reporter.utils.TokenHolder;

import java.io.File;
import java.nio.file.Paths;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This post-processor goes over existed OneMetricResult instances which where produced by
 * org.qubership.reporter.inspectors.impl.codequality.CodeCoverageBySonar
 * This post-processor collects all components into pages (chunks) and does call to sonarcloud.io
 * to fetch code-coverage metrics in bulk mode. This is needed for performance and throtling specifics.
 * After data is got - this processor updates OneMetricResult instances with new values.
 */
public class SonarMetricValuesBulkFulfilling extends AbstractPostInspector {
    private static final String CODE_COVERAGE_ID = new CodeCoverageBySonar().getMetric().getPersistenceId();
    private static final int PER_PAGE = 50;
    private static final ObjectMapper mapper = new ObjectMapper(new JsonFactory());
    static {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static final TypeReference<Map<String, Object>> type = new TypeReference<>() {};

    @Override
    public void doPostInspection(ReportModel reportModel, List<Map<String, Object>> allReposMetaData, Connection jdbcConnection) {
        // map to collect direct references to existed OneMetricResult instances
        Map<String, OneMetricResult> omrMap = new HashMap<>();

        // All component names which are neede to be processed by this post-processor
        List<String> sonarComponentNames = new ArrayList<>();

        // Step1: collect all known repositories to fetch data from Sonar for
        for (String repoName : reportModel.getRepositoryNames()) {
            for (Metric metric : reportModel.getMetrics()) {
                if (metric.getPersistenceId().equals(CODE_COVERAGE_ID)) {
                    OneMetricResult omResult = reportModel.getValue(repoName, metric.getPersistenceId());
                    String rawValue = omResult.getRawValue();

                    sonarComponentNames.add(rawValue);
                    omrMap.put(repoName, omResult);
                }
            }
        }

        // Step2: do bulk requests with paging & delay between requests not to fall into throtling
        while (!sonarComponentNames.isEmpty()) {
            List<String> nextChunk = MiscUtils.getChunk(sonarComponentNames, PER_PAGE);
            if (nextChunk.isEmpty()) break;

            sonarComponentNames.removeAll(nextChunk);

            // convert names to long string and build request url
            StringBuilder sb = new StringBuilder();
            for (String name : nextChunk) {
                if (sb.length() > 0) sb.append("%2C"); // comma delimiter
                sb.append(name);
            }
            String url = "https://sonarcloud.io/api/measures/search?metricKeys=coverage&projectKeys=" + sb;

            // do http GET call
            Map<String, String> sonarData = HttpUtils.doGet(url, new HttpUtils.IResponseHandler<Map<String, String>>() {
                @Override
                protected Map<String, String> onSuccess(CloseableHttpResponse httpResponse, String responseBody) throws Exception {
                    Map<String, String> result = new HashMap<>();

                    Map<String, Object> data = mapper.readValue(responseBody, type);
                    List<Map<String, Object>> mList = (List<Map<String, Object>>) data.get("measures");
                    for (Map<String, Object> mData : mList) {
                        String metricName = (String) mData.get("metric");
                        if ("coverage".equalsIgnoreCase(metricName)) {
                            String value = (String) mData.get("value");
                            String component = (String) mData.get("component");

                            result.put(component, value);
                        }
                    }
                    return result;
                }

                @Override
                protected Map<String, String> onError(int statusCode, CloseableHttpResponse httpResponse) throws Exception {
                    TheLogger.error("Error while processing " + url + ", HTTP-code is " + statusCode);
                    return null;
                }

                @Override
                protected Map<String, String> onException(Exception ex) {
                    TheLogger.error("Error while processing " + url, ex);
                    return null;
                }
            }, null);

            if (sonarData == null) {
                TheLogger.error("Error is returned for GET http request: " + url);
            }

            for (String nextComponent : nextChunk) {
                String sonarValue = sonarData == null ? "ERROR" : sonarData.get(nextComponent);
                String key = nextComponent;
                if (key.startsWith("Netcracker_")) key = key.substring(11);

                OneMetricResult omResult = omrMap.get(key);
                if (sonarValue != null) {
                    omResult.setRawValue(sonarValue + "%");
                    omResult.setSeverity(ResultSeverity.INFO);
                    omResult.setHttpReference("https://sonarcloud.io/project/overview?id=" + nextComponent);
                } else {
                    omResult.setRawValue("Not registered");
                    omResult.setSeverity(ResultSeverity.ERROR);
                    omResult.setHttpReference("https://sonarcloud.io/organizations/netcracker/projects");
                }
            }

            // do sleep if there are more data to process
            if (!sonarComponentNames.isEmpty())
                MiscUtils.sleep(2000);
        }
    }

    @Override
    public Metric getMetric() {
        return newMetric("sonar-coverage-metric-as-number", "Code Coverage %", MetricGroupsRegistry.CODE_QUALITY_GROUP);
    }


}
