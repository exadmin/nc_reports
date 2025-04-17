package org.qubership.reporter.inspectors.impl.codequality;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.qubership.reporter.inspectors.api.AbstractRepositoryInspector;
import org.qubership.reporter.inspectors.api.model.metric.Metric;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroup;
import org.qubership.reporter.inspectors.api.model.metric.MetricGroupsRegistry;
import org.qubership.reporter.inspectors.api.model.result.OneMetricResult;
import org.qubership.reporter.inspectors.api.model.result.ResultSeverity;
import org.qubership.reporter.utils.HttpUtils;
import org.qubership.reporter.utils.MiscUtils;
import org.qubership.reporter.utils.RepoUtils;
import org.qubership.reporter.utils.TheLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenedPRsInspector extends AbstractRepositoryInspector {
    private static final int SLEEP_BEFORE_NEXT_TRY_MILLIS = 5000;

    @Override
    protected OneMetricResult inspectRepoFolder(String pathToRepository, Map<String, Object> repoMetaData, List<Map<String, Object>> allReposMetaData) throws Exception {
        String url = RepoUtils.getPullsURL(repoMetaData);
        url = url.replaceAll("\\{/number}", "");

        url = url + "?state=open";

        int tryCount = 3;

        final OneMetricResult HTTP_ERROR_403 = new OneMetricResult(getMetric(), ResultSeverity.ERROR, "HTTP-403");

        while (tryCount > 0) {
            tryCount--;

            OneMetricResult omResult = HttpUtils.doGet(url, new HttpUtils.IResponseHandler<>() {
                @Override
                protected OneMetricResult onSuccess(CloseableHttpResponse httpResponse, String responseBody) throws Exception {
                    ObjectMapper mapper = new ObjectMapper(new JsonFactory());
                    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                    TypeReference<List<Map<String, Object>>> type = new TypeReference<>() {
                    };

                    List<Map<String, Object>> data = mapper.readValue(responseBody, type);
                    int numberOfOpenedPRs = data.size();

                    ResultSeverity severity = ResultSeverity.WARN;
                    if (numberOfOpenedPRs == 0) severity = ResultSeverity.OK;

                    OneMetricResult omResult = new OneMetricResult(getMetric(), severity, numberOfOpenedPRs + "");
                    // we return only number in this inspector, but all other extra data will be processed by post-processor

                    List<Map<String, String>> extraData = new ArrayList<>();
                    omResult.setExtraData("extra_data", extraData);

                    for (Map<String, Object> item : data) {
                        String createdAt = (String) item.get("created_at");
                        String title = (String) item.get("title");
                        String fromUser = (String) ((Map<String, Object>) item.get("user")).get("login");

                        Map<String, String> map = new HashMap<>();
                        map.put("created_at", createdAt);
                        map.put("title", title);
                        map.put("from_user", fromUser);
                        extraData.add(map);
                    }

                    return omResult;
                }

                @Override
                protected OneMetricResult onError(int statusCode, CloseableHttpResponse httpResponse) throws Exception {
                    if (statusCode == 403) return HTTP_ERROR_403;

                    return createError("HTTP CODE = " + statusCode);
                }

                @Override
                protected OneMetricResult onException(Exception ex) {
                    return createError("Exception " + ex);
                }

                private OneMetricResult createError(String titleText) {
                    OneMetricResult omResult = new OneMetricResult(getMetric(), ResultSeverity.ERROR, null);
                    omResult.setTitleText(titleText);
                    return omResult;
                }
            });

            // if rate limit triggered
            if (omResult == HTTP_ERROR_403) {
                TheLogger.debug("Throttling from github. Sleep for " + SLEEP_BEFORE_NEXT_TRY_MILLIS + "ms. Attempt left " + tryCount);
                MiscUtils.sleep(SLEEP_BEFORE_NEXT_TRY_MILLIS);
                continue;
            }

            return omResult;
        }

        TheLogger.debug("Failed to collect open-pull requests due to rate limiting");
        return error("GitHub didn't answered due to rate limiting");
    }

    @Override
    public Metric getMetric() {
        return newMetric("OpenedPRs", "Open Pull Requests Count", MetricGroupsRegistry.CODE_QUALITY_GROUP)
                .setDescriptionRef(""); // todo
    }
}
