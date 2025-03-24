package org.qubership.reporter.utils;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class HttpUtils {
    private static final int HTTP_CALL_TIMEOUT_SEC = 5;

    public static abstract class IResponseHandler<T> {
        protected abstract T onSuccess(CloseableHttpResponse httpResponse, String responseBody) throws Exception;
        protected abstract T onError(int statusCode, CloseableHttpResponse httpResponse) throws Exception;
        protected abstract T onException(Exception ex);
    }

    public static <T> T doGet(String uri, IResponseHandler<T> handler) {
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(HTTP_CALL_TIMEOUT_SEC * 1000).build();

        // Create an instance of HttpClient
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build()) {
            // Create an HTTP GET request
            HttpGet request = new HttpGet(uri);

            // Execute the request
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                // Get the status code
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    String responseBody = EntityUtils.toString(response.getEntity());
                    return handler.onSuccess(response, responseBody);
                } else {
                    return handler.onError(statusCode, response);
                }
            }
        } catch (Exception ex) {
            return handler.onException(ex);
        }
    }
}
