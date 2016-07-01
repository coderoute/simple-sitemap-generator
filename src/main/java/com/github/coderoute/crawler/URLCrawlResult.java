package com.github.coderoute.crawler;

import java.util.List;

public class URLCrawlResult {

    private List<CrawlURLAction> actions;

    private Links links;

    public URLCrawlResult(List<CrawlURLAction> actions, Links links) {
        this.actions = actions;
        this.links = links;
    }

    public List<CrawlURLAction> getActions() {
        return actions;
    }

    public Links getLinks() {
        return links;
    }
}
