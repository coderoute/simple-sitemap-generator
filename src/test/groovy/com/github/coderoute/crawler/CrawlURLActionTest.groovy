package com.github.coderoute.crawler

import org.junit.Rule
import spock.lang.Specification


class CrawlURLActionTest extends Specification {


    @Rule
    public MockWebsiteRule mockWebsiteRule = new MockWebsiteRule()

    def "download and parse contents of a URL"() {

        given:
            String url = mockWebsiteRule.createMockedUrlForHtmlResponse("/simple-page.htm", "simple-page.htm")
            SiteCrawler siteCrawler = Mock()

            def internalLinksUrls = ["http://localhost:3335/relative-path",
                                           "http://localhost:3335/internal-http-url",
                                           "https://localhost:3335/internal-https-url"] as Set
            def internalMediaURLs = ["http://localhost:3335/relative-path-image.jpg",
                                          "http://localhost:3335/internal-image.jpg"] as Set
            def externalLinks = ["http://example.org/external-image.jpg",
                                      "http://example.org/external-http-url",
                                      "https://example.org/external-https-url"] as Set

            def expectedLinks = new Links(url, internalLinksUrls, internalMediaURLs, externalLinks)

        when:
            CrawlURLAction underTest = new CrawlURLAction(url, siteCrawler)
            underTest.crawl()

        then:
            1 * siteCrawler.addLinks(expectedLinks)
            1 * siteCrawler.addCrawled(url)
    }
}
