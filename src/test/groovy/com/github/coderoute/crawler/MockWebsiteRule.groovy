package com.github.coderoute.crawler

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockClassRule
import com.google.common.base.Charsets
import com.google.common.base.Preconditions
import com.google.common.io.Resources
import groovy.transform.CompileStatic
import org.apache.commons.io.FileUtils
import org.apache.http.HttpHeaders
import org.apache.http.HttpStatus

@CompileStatic
class MockWebsiteRule extends WireMockClassRule {

    public static final int MOCK_WEBSITE_PORT = 3335

    public MockWebsiteRule() {
        super(MOCK_WEBSITE_PORT)
    }

    String createMockedUrlForHtmlResponse(String relativePath, String responseFileName) {

        String responseBody = readResourceFileToString(responseFileName)

        stubFor(WireMock.get(WireMock.urlEqualTo(relativePath))
                .willReturn(WireMock.aResponse().withStatus(HttpStatus.SC_OK)
                                                .withHeader(HttpHeaders.CONTENT_TYPE, "text/html;charset=utf-8")
                                                .withBody(responseBody)));


        return generateURL(relativePath)
    }

    private static String readResourceFileToString(String resourceFileName) {
        def resourceUrl = Resources.getResource("mocked-webpages/" + resourceFileName).path
        def filePath = URLDecoder.decode(resourceUrl, Charsets.UTF_8.toString())
        FileUtils.readFileToString(new File(filePath), Charsets.UTF_8)
    }

    public static String generateURL(String relativePath) {
        Preconditions.checkArgument(relativePath.startsWith("/"))
        return "http://localhost:${MOCK_WEBSITE_PORT}${relativePath}"
    }
}
