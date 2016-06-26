Introduction
============

This project is an implementation of a simple sitemap generator. The sitemap is printed to stdout, and comprises of 3 parts:
1. Links to all pages under the same domain
2. Links to static content items such as images
3. External URLs.

The implementation requires a starting URL as input. The application then crawls all pages within the domain, but does not follow the links to external sites.

Design Notes
============
The implementation uses a Fork-Join pool and recursively creates new tasks to crawl the linked pages. Links are in page are parsed using Jsoup and then categorized as internal-link, internal-media, or external link.

The implementation internally keeps track of how the linked pages are discovered and which ones cannot be downloaded due to errors. The output can thus be extended to include extra information if desired.

Tests are written using Spock-framework. Only integration tests have been implemented as most logic ic closely related to actual network connections and parsing of HTML. The tests use WireMock (provides an embedded jetty server) to simulate the target site to be crawled.

Limitations & Possible improvements
===================================
1. Requests are not retried in case of connection timeout errors.
2. Number of concurrent threads is currently fixed at 4. The number is kept low to avoid excessive load on target server.
3. There is no logic related to delays between requests.
4. Implementation is a stand-alone applications and is not designed to be run as a distributed system. 
5. Check response header's content-type and abort download if the content is not html.
6. Attempt to encode URLs with spaces.
7. Output could be saved as a file.
8. The crawler could be made to cope with large websites by:
	a. Writing the output incrementally as crawling progresses.
	b. Keeping track of crawled pages in an external storage.

Build & Run Instructions
========================
For instructions on how to build the project and run the sitemap generator, please see build-instructions.txt


