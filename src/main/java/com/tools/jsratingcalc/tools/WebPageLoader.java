package com.tools.jsratingcalc.tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class WebPageLoader {
    public Document getContent(String requestUrl) {
        try {
            return Jsoup.connect(requestUrl).get();
        } catch (IOException e) {
            throw new RuntimeException("Couldn't get results from " + requestUrl, e);
        }
    }
}
