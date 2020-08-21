package com.tools.jsratingcalc.service;

import com.tools.jsratingcalc.domain.RatingResults;

public interface JSRatingCalcService {
    RatingResults calculateRatingForSearchTerm(String term);
}
