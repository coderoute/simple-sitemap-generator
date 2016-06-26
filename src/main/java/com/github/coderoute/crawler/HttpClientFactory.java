package com.github.coderoute.crawler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class HttpClientFactory {

    private static final int DEFAULT_CONNECTION_TIMEOUT_MS = 30_000;
    private static final int DEFAULT_SOCKET_TIMEOUT_MS = 30_000;
    private static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 20;

    private static final HttpClient httpClient = create();

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    private static HttpClient create() {

            PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
            connectionManager.setMaxTotal(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);
            connectionManager.setDefaultMaxPerRoute(DEFAULT_MAX_CONNECTIONS_PER_ROUTE);

            HttpClientBuilder httpClientBuilder = HttpClientBuilder
                    .create()
                    .setDefaultRequestConfig(
                            RequestConfig.copy(RequestConfig.DEFAULT)
                                    .setConnectTimeout(DEFAULT_CONNECTION_TIMEOUT_MS)
                                    .setConnectionRequestTimeout(DEFAULT_CONNECTION_TIMEOUT_MS)
                                    .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT_MS)
                                    .build())
                    .setConnectionManager(connectionManager);

            return httpClientBuilder.build();
        }
}
