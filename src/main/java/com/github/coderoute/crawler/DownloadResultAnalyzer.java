package com.github.coderoute.crawler;

import com.github.coderoute.crawler.models.DownloadResult;
import com.github.coderoute.crawler.models.Links;
import com.github.coderoute.crawler.models.URLCrawlResult;

import java.util.List;
import java.util.stream.Collectors;

public class DownloadResultAnalyzer {

    private SiteCrawler siteCrawler;

    public DownloadResultAnalyzer(SiteCrawler siteCrawler) {
        this.siteCrawler = siteCrawler;
    }

    public URLCrawlResult analyze(DownloadResult downloadResult) {
        LinksExtractor linksExtractor = new LinksExtractor();
        Links links = linksExtractor.extractLinks(downloadResult);

        List<CrawlURLAction> actions = links.getInternalLinkedURLs().stream().filter(link -> !siteCrawler.isCrawled(link))
                .map(link -> new CrawlURLAction(link, siteCrawler)).collect(Collectors.toList());
        return new URLCrawlResult(actions, links);
    }
}
