package com.github.coderoute.crawler;

import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class URLCrawler extends RecursiveAction {

    private static final Logger LOGGER = getLogger(URLCrawler.class);
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

        try {
            URI uri = new URI(pageURL);
            DownloadResult downloadResult = urlDownloader.fetch(uri);
            return linksExtractor.extractLinks(downloadResult);
        } catch (URISyntaxException e) {
            LOGGER.warn("Got URISyntaxException for ["+pageURL+"]");
            return Links.empty(pageURL);
        }
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
