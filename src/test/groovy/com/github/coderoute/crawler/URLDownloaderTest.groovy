package com.github.coderoute.crawler

import com.github.coderoute.crawler.models.DownloadResult
import spock.lang.Specification


class URLDownloaderTest extends Specification {

    URLDownloader underTest = new URLDownloader()

    def "download URL for a syntactically incorrect URL results in ERROR"() {

        when:
        DownloadResult downloadResult = underTest.fetch("http://www.example.org/some document.htm", 0)

        then:
        downloadResult.downloadResultType == DownloadResult.DownloadResultType.INVALID_URI

    }
}
