package com.github.coderoute.crawler;

import com.github.coderoute.crawler.models.DownloadResult;
import com.github.coderoute.crawler.models.URLCrawlResult;
import com.google.common.annotations.VisibleForTesting;

import java.util.concurrent.RecursiveAction;

public class CrawlURLAction extends RecursiveAction {

    private final SiteCrawler siteCrawler;
    private final String pageUrl;

    public CrawlURLAction(String pageUrl, SiteCrawler siteCrawler) {
        this.pageUrl = pageUrl;
        this.siteCrawler = siteCrawler;
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
        DownloadResult downloadResult = urlDownloader.fetch(this.pageUrl);

        DownloadResultAnalyzer downloadResultAnalyzer = new DownloadResultAnalyzer(siteCrawler);
        URLCrawlResult urlCrawlResult = downloadResultAnalyzer.analyze(downloadResult);

        this.siteCrawler.addCrawled(pageUrl);
        this.siteCrawler.addLinks(urlCrawlResult.getLinks());
        return urlCrawlResult;
    }

}
