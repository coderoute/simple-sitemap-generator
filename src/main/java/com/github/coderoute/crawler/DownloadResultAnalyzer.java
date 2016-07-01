package com.github.coderoute.crawler;

import com.github.coderoute.crawler.models.DownloadResult;
import com.github.coderoute.crawler.models.Links;
import com.github.coderoute.crawler.models.URLCrawlResult;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DownloadResultAnalyzer {

    public static final int RETRY_THRESHOLD = 2;

    private SiteCrawler siteCrawler;

    public DownloadResultAnalyzer(SiteCrawler siteCrawler) {
        this.siteCrawler = siteCrawler;
    }

    public URLCrawlResult analyze(DownloadResult downloadResult) {
        if(downloadResult.getDownloadResultType().equals(DownloadResult.DownloadResultType.IO_ERROR)) {
            if(downloadResult.getTryCount() >= RETRY_THRESHOLD) {
                return new URLCrawlResult(new ArrayList<>(), Links.empty(downloadResult.getRequestURL()));
            } else {
                CrawlURLAction retry = new CrawlURLAction(downloadResult.getRequestURL(), siteCrawler, downloadResult.getTryCount() + 1);
                return new URLCrawlResult(ImmutableList.of(retry), Links.empty(downloadResult.getRequestURL()));
            }
        }

        LinksExtractor linksExtractor = new LinksExtractor();
        Links links = linksExtractor.extractLinks(downloadResult);

        List<CrawlURLAction> actions = links.getInternalLinkedURLs().stream().filter(link -> !siteCrawler.isCrawled(link))
                .map(link -> new CrawlURLAction(link, siteCrawler, 0)).collect(Collectors.toList());
        return new URLCrawlResult(actions, links);
    }
}
