package com.github.coderoute.crawler;

import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class URLDownloader {

    private static final Logger LOGGER = getLogger(URLDownloader.class);

    public DownloadResult fetch(URI requestUri) {
        HttpGet httpGet = new HttpGet(requestUri);
        String requestURL = requestUri.toString();
        try {
            HttpClient httpClient = HttpClientFactory.getHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            Optional<String> contentType = getContentType(httpResponse);

            if(HttpStatus.SC_OK == statusCode) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return new DownloadResult(requestURL, DownloadResult.DownloadResultType.SUCCESS, contentType, responseBody);
            } else {
                return new DownloadResult(requestURL, DownloadResult.DownloadResultType.ERROR, contentType, "HTTP response status was: " + statusCode);
            }
        } catch (IOException e) {
            LOGGER.error("Error fetching requestUri["+ requestURL +"] ", e);
            return new DownloadResult(requestURL, DownloadResult.DownloadResultType.ERROR, Optional.empty(), e.toString());
        } finally {
            httpGet.releaseConnection();
        }
    }

    private Optional<String> getContentType(HttpResponse httpResponse) {
        Header contentTypeHeader = httpResponse.getFirstHeader(HttpHeaders.CONTENT_TYPE);
        if(contentTypeHeader != null) {
            return Optional.of(contentTypeHeader.getValue());
        }
        return Optional.empty();
    }

}
