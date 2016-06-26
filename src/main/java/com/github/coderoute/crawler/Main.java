package com.github.coderoute.crawler;

public class Main {

    public static void main(String[] args) {
        if(args.length != 1) {
            System.out.println("Usage: java -jar build/libs/simple-sitemap-generator-*.jar [http://base-uri]");
            System.exit(1);
        }
        String baseUri = args[0];
        SiteCrawler siteCrawler = new SiteCrawler();
        SiteMap sitemap = siteCrawler.buildSiteMap(baseUri, 4);
        SiteMapPrinter.print(sitemap);
    }
}
