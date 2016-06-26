package com.github.coderoute.crawler;

import java.util.Optional;

public class DownloadResult {

    private final String requestURL;
    private final DownloadResultType downloadResultType;
    private final Optional<String> contentType;
    private final String content;


    public DownloadResult(String requestURL, DownloadResultType resultType, Optional<String> contentType, String content) {
        this.requestURL = requestURL;
        this.downloadResultType = resultType;
        this.contentType = contentType;
        this.content = content;
    }

    enum DownloadResultType {
        SUCCESS, ERROR
    }

    public String content() {
        return content;
    }

    public DownloadResultType getDownloadResultType() {
        return downloadResultType;
    }

    public Optional<String> getContentType() {
        return contentType;
    }

    public String getRequestURL() {
        return requestURL;
    }
}
