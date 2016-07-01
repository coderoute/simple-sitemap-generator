package com.github.coderoute.crawler

import spock.lang.Specification


class DownloadResultAnalyzerTest extends Specification {

    def "Given a download result, more URL crawling tasks are generated if needed"() {

        given:
            DownloadResultAnalyzer underTest = new DownloadResultAnalyzer()

        when:
            URLCrawlResult result = underTest.analyze(downloadResult)

        then:
            result.links == Links.empty("someURL")
            result.actions == []

        where:

        downloadResult  << [ new DownloadResult("someURL", DownloadResult.DownloadResultType.INVALID_URI, Optional.empty(), "")]


    }
}
