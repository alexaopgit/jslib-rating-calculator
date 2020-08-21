package com.tools.jsratingcalc.tools;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class GoogleCrawler {

    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?q=%s&num=10";

    private final WebPageLoader webPageLoader;

    public List<URL> loadTopLinksFromGoogle(String searchTerm) {
        String requestUrl = String.format(GOOGLE_SEARCH_URL, searchTerm);
        Document plainHtmlPageContent = webPageLoader.getContent(requestUrl);

        return parseLinks(plainHtmlPageContent).stream()
                .filter(link -> !StringUtils.isEmpty(link))
                .map(this::stringToUrl)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    List<String> parseLinks(Document doc) {
        return doc.select(".r").stream()
                .map(element -> element.child(0).attr("href"))
                .collect(Collectors.toList());
    }

    private URL stringToUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
