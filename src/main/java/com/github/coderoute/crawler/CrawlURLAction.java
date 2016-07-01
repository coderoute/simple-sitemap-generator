package com.github.coderoute.crawler;

import com.github.coderoute.crawler.models.DownloadResult;
import com.github.coderoute.crawler.models.URLCrawlResult;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.concurrent.RecursiveAction;

public class CrawlURLAction extends RecursiveAction {

    private final SiteCrawler siteCrawler;
    private final String pageUrl;
    private final int tryCount;

    public CrawlURLAction(String pageUrl, SiteCrawler siteCrawler, int tryCount) {
        this.pageUrl = pageUrl;
        this.siteCrawler = siteCrawler;
        this.tryCount = tryCount;
    }


    @Override
    protected void compute() {
        if(this.siteCrawler.isCrawled(this.pageUrl)) {
            return;
        }

        URLCrawlResult urlCrawlResult = crawl();

        invokeAll(urlCrawlResult.getActions());
    }

    @VisibleForTesting
    URLCrawlResult crawl() {
        URLDownloader urlDownloader = new URLDownloader();
        DownloadResult downloadResult = urlDownloader.fetch(this.pageUrl, tryCount);

        DownloadResultAnalyzer downloadResultAnalyzer = new DownloadResultAnalyzer(siteCrawler);
        URLCrawlResult urlCrawlResult = downloadResultAnalyzer.analyze(downloadResult);

        this.siteCrawler.addCrawled(pageUrl);
        this.siteCrawler.addLinks(urlCrawlResult.getLinks());
        return urlCrawlResult;
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
