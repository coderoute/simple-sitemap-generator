package com.github.coderoute.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LinksExtractor {

    public Links extractHtmlLinks(String requestUrl, String content) {

        BaseUri baseUri = getBaseUri(requestUrl);
        Document document = Jsoup.parse(content, baseUri.getUri());
        Elements anchorElements = document.select("a[href]");
        Elements mediaElements = document.select("[src]");

        Set<String> mediaLinks = new HashSet<>();
        Set<String> internalLinks = new HashSet<>();
        Set<String> externalLinks = new HashSet<>();

        List<String> mediaUrls = mediaElements.stream().map(src -> src.attr("abs:src")).collect(Collectors.toList());
        List<String> links = anchorElements.stream().map(src -> src.attr("abs:href")).collect(Collectors.toList());

        categorizeLinks(baseUri, mediaUrls, mediaLinks, externalLinks);
        categorizeLinks(baseUri, links, internalLinks, externalLinks);

        return new Links(requestUrl, internalLinks, mediaLinks, externalLinks);
    }

    private void categorizeLinks(BaseUri baseUri, List<String> urls, Set<String> mediaLinks, Set<String> externalLinks) {
        for (String url : urls) {

            if(url.startsWith(baseUri.forHttp()) || url.startsWith(baseUri.forHttps())) {
                mediaLinks.add(url);
            } else {
                externalLinks.add(url);
            }
        }
    }

    public Links extractLinks(DownloadResult downloadResult) {

        if(downloadResult.getDownloadResultType().equals(DownloadResult.DownloadResultType.SUCCESS) &&
                downloadResult.getContentType().isPresent() && downloadResult.getContentType().get().contains("html")) {

            return this.extractHtmlLinks(downloadResult.getRequestURL(), downloadResult.content());
        }
        return Links.empty(downloadResult.getRequestURL());
    }

    private static final Pattern baseUriPattern = Pattern.compile("([^/]+)://([^/]+)/.*");

    public static BaseUri getBaseUri(String url) {
        Matcher matcher = baseUriPattern.matcher(url);
        if(matcher.matches() && matcher.groupCount() == 2) {
            return new BaseUri(matcher.group(1) + "://" + matcher.group(2) + "/", matcher.group(2));
        }
        throw new IllegalStateException("Got a DownloadResult for malformed URL["+url+"]");
    }

    private static class BaseUri {
        private final String uri;
        private final String domain;
        BaseUri(String uri, String domain) {
            this.uri = uri;
            this.domain = domain;
        }

        public String getUri() {
            return uri;
        }

        public String forHttp() {
            return "http://" + domain + "/";
        }

        public String forHttps() {
            return "https://" + domain + "/";
        }
    }
}
