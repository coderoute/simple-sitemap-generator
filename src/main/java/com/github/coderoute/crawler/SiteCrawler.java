package com.github.coderoute.crawler;

import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

import static org.slf4j.LoggerFactory.getLogger;

public class SiteCrawler {

    private static final Logger LOGGER = getLogger(SiteCrawler.class);

    private final Set<String> linksCrawled = new HashSet<>();

    private final SiteMap sitemap = new SiteMap();

    public SiteMap buildSiteMap(String baseUri, int numThreads) {
        ForkJoinPool forkJoinPool = new ForkJoinPool(numThreads);
        ForkJoinTask<Void> forkJoinTask = forkJoinPool.submit(new URLCrawler(baseUri, this));
        forkJoinTask.join();
        return sitemap;
    }

    public void addCrawled(String link) {
        boolean isNew = this.linksCrawled.add(link);
        if(isNew && LOGGER.isDebugEnabled()) {
            LOGGER.debug("Crawled: " + link);
        }
    }

    public boolean isCrawled(String link) {
        return this.linksCrawled.contains(link);
    }

    public void addLinks(Links links) {
        this.sitemap.addLinks(links);
    }
}
