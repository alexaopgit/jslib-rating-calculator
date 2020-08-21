package com.tools.jsratingcalc;

import com.tools.jsratingcalc.domain.RatingResults;
import com.tools.jsratingcalc.service.JSRatingCalcService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@AllArgsConstructor
@Component
public class JSRatingCalculator implements CommandLineRunner {
    private static final Integer NUMBER_OF_LIBS_IN_TOP = 5;

    private final JSRatingCalcService jsRatingCalcService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Please provide you search term");
        BufferedReader buffReader = new BufferedReader(new InputStreamReader(System.in));
        String searchTerm = buffReader.readLine();

        System.out.println("Usage calculation...");
        final RatingResults ratingResults = jsRatingCalcService.calculateRatingForSearchTerm(searchTerm);

        System.out.println("Top " + NUMBER_OF_LIBS_IN_TOP + " most used JS libs:");
        List<String> topJsLibs = ratingResults.getTopNInRating(NUMBER_OF_LIBS_IN_TOP);
        System.out.println(String.join("\n", topJsLibs));
    }
}
