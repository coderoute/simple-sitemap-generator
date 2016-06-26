package com.github.coderoute.crawler;

import java.util.Set;

public class SiteMapPrinter {

    public static void print(SiteMap siteMap) {
        printLinks("Internal URLs:", siteMap.getInternalLinkedURLs());

        printLinks("Internal Media URLs", siteMap.getInternalMediaURLs());

        printLinks("External URLs", siteMap.getExternalLinks());
    }

    private static void printLinks(String label, Set<String> urls) {
        System.out.println(label);
        urls.stream().forEach(System.out::println);
    }
}
