package com.tools.jsratingcalc.tools;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@AllArgsConstructor
public class JSLibsParser {

    private final WebPageLoader webPageLoader;

    public List<String> getAllJsLibsFromPage(URL url) {
        log.info("analysing: {}", url);

        Document document = webPageLoader.getContent(url.toString());

        return parseJsLibs(document).stream()
                // somewhere here we could inject deduplication alg.
                .map(this::tripToLibName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    /**
     * This logic could be more sophisticated, but it should be enough for this example
     */
    String tripToLibName(String libFullPath) {
        String result = null;
        int libEndIndex = libFullPath.indexOf(".js");
        if (libEndIndex > 0) {
            String liebIter1 = libFullPath.substring(0, libEndIndex + ".js".length());
            int start = liebIter1.lastIndexOf("/");
            if (start > 0) {
                result = liebIter1.substring(start + 1);
            } else {
                result = liebIter1;
            }
        }
        return result;
    }

    List<String> parseJsLibs(Document document) {
        return document.select("script[src]").stream()
                .map(element -> element.attr("src"))
                .collect(Collectors.toList());
    }
}
