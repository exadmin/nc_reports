package org.qubership.reporter.inspectors.impl.codequality;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.utils.FileUtils;
import org.qubership.reporter.utils.StrUtils;
import org.qubership.reporter.utils.TheLogger;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeCoverageBySonar extends AbstractRepositoryInspector {
    private static final int HTTP_CALL_TIMEOUT_SEC = 5;

    @Override
    public Metric getMetric() {
        return newMetric("Sonar Code Coverage", "Sonar Code Coverage", MetricGroupsRegistry.CODE_QUALITY_GROUP)
                .setDescription("Returns coverage metric from Sonar Cloud service for the component");
    }

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        // define if sonar-call is registered in some github-action
        String prjKey = findSonarProjectKeyValue(pathToRepository);
        if (StrUtils.isEmpty(prjKey)) {
            return error("Not registered");
        }

        // otherwise doing call to sonar-cloud
        String value = getMetricValueFromSonarCloud(prjKey, "coverage");
        if (value.startsWith("Error:")) {
            return error(value);
        }

        OneMetricResult result = ok(value);
        result.setHttpReference("https://sonarcloud.io/summary/overall?id=" + prjKey + "&branch=main");

        return result;
    }

    private static final Pattern REGEXP_PRJ_KEY = Pattern.compile("\\bDsonar\\.projectKey\\s*=\\s*([^\\s]+)\\b", Pattern.CASE_INSENSITIVE);

    private static String findSonarProjectKeyValue(String pathToRepository) {
        // find all *.yml files in the ./workflow directory and fetch special key
        List<String> yamlFiles = FileUtils.findAllFilesRecursively(pathToRepository + "/.github/workflows", new FileUtils.FileAcceptor() {
            @Override
            public boolean testFileByName(String shortFileName) {
                return shortFileName.endsWith(".yml");
            }
        });

        for (String nextFile : yamlFiles) {
            try {
                String fileBody = FileUtils.readFile(nextFile);
                Matcher matcher = REGEXP_PRJ_KEY.matcher(fileBody);
                if (matcher.find()) {
                    String foundValue = matcher.group(1);
                    if (StrUtils.isNotEmpty(foundValue)) return foundValue;
                }
            } catch (IOException ex) {
                TheLogger.error("Error while reading file " + nextFile, ex);
                // continue
            }
        }

        return null;
    }

    private static String getMetricValueFromSonarCloud(String sonarComponentName, String metricsCommaSeparated) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CALL_TIMEOUT_SEC * 1000).build();

        // Create an instance of HttpClient
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()) {
            // Create an HTTP GET request
            HttpGet request = new HttpGet("https://sonarcloud.io/api/measures/component?component="+ sonarComponentName + "&metricKeys=" + metricsCommaSeparated);

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the status code
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {

                    // Get the response body
                    String responseBody = EntityUtils.toString(response.getEntity());

                    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    TypeReference<Map<String, Object>> type = new TypeReference<>() {};

                    Map<String, Object> data = mapper.readValue(responseBody, type);

                    Map<String, Object> componentData = (Map<String, Object>) data.get("component");
                    if (componentData == null) return "Error: parsing response. Component is absent.";

                    List measuresData = (List) componentData.get("measures");
                    if (measuresData == null) return "Error: parsing response. Measures are absent.";

                    for (Object next : measuresData) {
                        Map<String, Object> measure = (Map<String, Object>) next;
                        String key = (String) measure.get("metric");
                        if ("coverage".equals(key)) {
                            String value = (String) measure.get("value");
                            if (StrUtils.isEmpty(value))
                                return "Error: parsing response. No 'value' for coverage metric";

                            return value + "%";
                        }
                    }

                    return "Error: No 'coverage' metric is returned in response.";
                } else {
                    return "Error: HTTP_CODE = " + statusCode;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.toString();
        }
    }
}
