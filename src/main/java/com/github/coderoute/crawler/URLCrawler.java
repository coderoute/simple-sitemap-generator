package com.github.coderoute.crawler;

import com.google.common.annotations.VisibleForTesting;

import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

public class URLCrawler extends RecursiveAction {

    private final SiteCrawler siteCrawler;
    private final String pageUrl;

    public URLCrawler(String pageUrl, SiteCrawler siteCrawler) {
        this.pageUrl = pageUrl;
        this.siteCrawler = siteCrawler;
    }

    @VisibleForTesting
    static Links crawlPage(String pageURL) {

        URLDownloader urlDownloader = new URLDownloader();
        LinksExtractor linksExtractor = new LinksExtractor();

        DownloadResult downloadResult = urlDownloader.fetch(pageURL);
        return linksExtractor.extractLinks(downloadResult);
    }

    @Override
    protected void compute() {
        if(this.siteCrawler.isCrawled(this.pageUrl)) {
            return;
        }

        Links links = crawlPage(this.pageUrl);
        this.siteCrawler.addCrawled(pageUrl);
        this.siteCrawler.addLinks(links);

        List<URLCrawler> actions = links.getInternalLinkedURLs().stream().filter(link -> !siteCrawler.isCrawled(link))
                .map(link -> new URLCrawler(link, siteCrawler)).collect(Collectors.toList());
        invokeAll(actions);
    }


}
