package com.tools.jsratingcalc.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RatingResults {
    Map<String, Integer> jsLibsRating = new ConcurrentHashMap<>();

    public void addJsLibs(List<String> jsLibs) {
        jsLibs.forEach(jsLib -> {
            Integer numberOfOccurrences = jsLibsRating.get(jsLib);
            if (numberOfOccurrences != null) {
                numberOfOccurrences++;
            } else {
                numberOfOccurrences = 1;
            }
            jsLibsRating.put(jsLib, numberOfOccurrences);
        });
    }

    public static Map<String, Integer> sortByValue(final Map<String, Integer> jsLibsRating) {
        return jsLibsRating.entrySet().stream()
                .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public List<String> getTopNInRating(int countInTop) {
        return sortByValue(jsLibsRating).keySet().stream()
                .limit(countInTop)
                .collect(Collectors.toList());
    }

}
