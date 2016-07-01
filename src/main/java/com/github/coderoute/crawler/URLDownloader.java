package com.github.coderoute.crawler;

import com.github.coderoute.crawler.models.DownloadResult;
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
import java.net.URISyntaxException;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

public class URLDownloader {

    private static final Logger LOGGER = getLogger(URLDownloader.class);

    public DownloadResult fetch(String requestUri, int tryCount) {
        try {
            return fetch(new URI(requestUri), tryCount);
        } catch (URISyntaxException e) {
            LOGGER.warn("Got URISyntaxException for ["+requestUri+"]");
            return new DownloadResult(requestUri, tryCount, DownloadResult.DownloadResultType.INVALID_URI, Optional.empty(), "");
        }
    }

    public DownloadResult fetch(URI requestUri, int tryCount) {
        HttpGet httpGet = new HttpGet(requestUri);
        String requestURL = requestUri.toString();
        try {
            HttpClient httpClient = HttpClientFactory.getHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            Optional<String> contentType = getContentType(httpResponse);

            if(HttpStatus.SC_OK == statusCode) {
                String responseBody = EntityUtils.toString(httpResponse.getEntity());
                return new DownloadResult(requestURL, tryCount, DownloadResult.DownloadResultType.SUCCESS, contentType, responseBody);
            } else {
                return new DownloadResult(requestURL, tryCount, DownloadResult.DownloadResultType.ERROR_RESPONSE, contentType, "HTTP response status was: " + statusCode);
            }
        } catch (IOException e) {
            LOGGER.error("Error fetching requestUri["+ requestURL +"] ", e);
            return new DownloadResult(requestURL, tryCount, DownloadResult.DownloadResultType.IO_ERROR, Optional.empty(), e.toString());
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
