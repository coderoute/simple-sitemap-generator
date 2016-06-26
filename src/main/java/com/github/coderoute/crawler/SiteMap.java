package com.github.coderoute.crawler;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class SiteMap {

    private final Set<String> internalLinkedURLs;
    private final Set<String> internalMediaURLs;
    private final Set<String> externalLinks;

    public SiteMap() {
        this.internalLinkedURLs = new ConcurrentSkipListSet<>();
        this.internalMediaURLs = new ConcurrentSkipListSet<>();
        this.externalLinks = new ConcurrentSkipListSet<>();
    }

    public void addLinks(Links links) {
        this.internalLinkedURLs.addAll(links.getInternalLinkedURLs());
        this.internalMediaURLs.addAll(links.getInternalMediaURLs());
        this.externalLinks.addAll(links.getExternalLinks());
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
}
