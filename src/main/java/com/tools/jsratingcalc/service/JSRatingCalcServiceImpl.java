package com.tools.jsratingcalc.service;

import com.tools.jsratingcalc.domain.RatingResults;
import com.tools.jsratingcalc.tools.GoogleCrawler;
import com.tools.jsratingcalc.tools.JSLibsParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class JSRatingCalcServiceImpl implements JSRatingCalcService {

    private final GoogleCrawler googleCrawler;
    private final JSLibsParser jsLibsParser;

    @Override
    public RatingResults calculateRatingForSearchTerm(String searchTerm) {
        // calling google to collect top links
        List<URL> urlList = googleCrawler.loadTopLinksFromGoogle(searchTerm);

        // load each page and collect all used JS libs
        final List<CompletableFuture<List<String>>> futures = urlList.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> jsLibsParser.getAllJsLibsFromPage(url)))
                .collect(Collectors.toList());

        // waiting for all executions
        CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).whenComplete((completed, ex) -> {
            if (ex != null) {
                log.error(ex.getMessage(), ex);
            } else {
                log.info("All calculations have been done");
            }
        }).join();

        // collect results
        RatingResults ratingResults = new RatingResults();

        futures.stream()
                .map(CompletableFuture::join)
                .forEach(ratingResults::addJsLibs);

        return ratingResults;
    }
}
