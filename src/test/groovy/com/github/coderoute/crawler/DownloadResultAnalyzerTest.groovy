package com.github.coderoute.crawler

import com.github.coderoute.crawler.models.DownloadResult
import com.github.coderoute.crawler.models.Links
import com.github.coderoute.crawler.models.URLCrawlResult
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll


class DownloadResultAnalyzerTest extends Specification {

    public static final String TEST_URL = "someURL"

    @Shared
    SiteCrawler siteCrawler = Mock()

    @Unroll
    def "Given a download result, more URL crawling tasks are generated when needed"() {

        given:
            DownloadResultAnalyzer underTest = new DownloadResultAnalyzer(siteCrawler)

        when:
            URLCrawlResult result = underTest.analyze(downloadResult)

        then:
            result.links == Links.empty(TEST_URL)
            result.actions == expectedActions

        where:

        downloadResult  << [
                new DownloadResult(TEST_URL, 0, DownloadResult.DownloadResultType.INVALID_URI, Optional.empty(), ""),
                new DownloadResult(TEST_URL, 0, DownloadResult.DownloadResultType.IO_ERROR, Optional.empty(), ""),
                new DownloadResult(TEST_URL, DownloadResultAnalyzer.RETRY_THRESHOLD, DownloadResult.DownloadResultType.IO_ERROR, Optional.empty(), "")
        ]

        expectedActions << [
                [],
                [ new CrawlURLAction(TEST_URL, siteCrawler, 1) ],
                []
        ]
    }
}
