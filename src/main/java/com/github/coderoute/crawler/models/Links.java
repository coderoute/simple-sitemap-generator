package com.github.coderoute.crawler.models;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashSet;
import java.util.Set;

public class Links {

    private final String originUrl;
    private final Set<String> internalLinkedURLs;
    private final Set<String> internalMediaURLs;
    private final Set<String> externalLinks;

    public Links(String originUrl, Set<String> internalLinkedURLs, Set<String> internalMediaURLs, Set<String> externalLinks) {
        this.originUrl = originUrl;
        this.internalLinkedURLs = internalLinkedURLs;
        this.internalMediaURLs = internalMediaURLs;
        this.externalLinks = externalLinks;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public Set<String> getInternalLinkedURLs() {
        return internalLinkedURLs;
    }

    public Set<String> getInternalMediaURLs() {

        return internalMediaURLs;
    }

    public Set<String> getExternalLinks() {
        return externalLinks;
    }

    public static Links empty(String requestUrl) {
        return new Links(requestUrl, new HashSet<>(), new HashSet<>(), new HashSet<>());
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
