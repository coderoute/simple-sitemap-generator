package com.github.coderoute.crawler

import org.junit.Rule
import spock.lang.Specification

class SiteCrawlerTest extends Specification {

    @Rule
    public MockWebsiteRule mockWebsiteRule = new MockWebsiteRule()

    def "build SiteMap"() {

        given:
            SiteCrawler underTest = new SiteCrawler()
            mockWebsiteRule.createMockedUrlForHtmlResponse("/site-index.htm", "site-index.htm")
            mockWebsiteRule.createMockedUrlForHtmlResponse("/site-section-1.htm", "site-section-1.htm")
            mockWebsiteRule.createMockedUrlForHtmlResponse("/site-section-2.htm", "site-section-2.htm")

        when:
            SiteMap result = underTest.buildSiteMap(MockWebsiteRule.generateURL("/site-index.htm"), 4)

        then:
            result.externalLinks == ["http://www.example.org"] as Set
            result.internalLinkedURLs == [MockWebsiteRule.generateURL("/site-index.htm"),
                                         MockWebsiteRule.generateURL("/site-section-1.htm"),
                                         MockWebsiteRule.generateURL("/site-section-2.htm"),
                                         MockWebsiteRule.generateURL("/broken-link.htm"),
                                         MockWebsiteRule.generateURL("/link with spaces.htm")
                                        ] as Set
            result.internalMediaURLs == [] as Set

    }
}
