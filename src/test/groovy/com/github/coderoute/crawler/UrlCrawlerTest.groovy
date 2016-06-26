package com.github.coderoute.crawler

import org.junit.Rule
import spock.lang.Specification


class UrlCrawlerTest extends Specification {


    @Rule
    public MockWebsiteRule mockWebsiteRule = new MockWebsiteRule()

    def "download and parse contents of a URL"() {

        given:
            String url = mockWebsiteRule.createMockedUrlForHtmlResponse("/simple-page.htm", "simple-page.htm")

        when:
            Links result = URLCrawler.crawlPage(url)

        then:
            result.getInternalLinkedURLs() == ["http://localhost:3335/relative-path",
                                               "http://localhost:3335/internal-http-url",
                                               "https://localhost:3335/internal-https-url"] as Set
            result.getInternalMediaURLs() == ["http://localhost:3335/relative-path-image.jpg",
                                              "http://localhost:3335/internal-image.jpg"] as Set
            result.getExternalLinks() == ["http://example.org/external-image.jpg",
                                          "http://example.org/external-http-url",
                                          "https://example.org/external-https-url"] as Set
    }
}
