package com.crawler.crawler;

import com.crawler.crawler.model.Element;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Crawler {

    Logger logger = LoggerFactory.getLogger(Crawler.class);

    public void execute(){
        Connection connection = getConnection();
        try {
            Document doc = connection.get();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private Connection getConnection() {
        return Jsoup.connect("https://baijiahao.baidu.com/s?id=1626678723346182275&wfr=spider&for=pc")
                    .userAgent("Mozilla")
                    .cookie("auth", "token")
                    .timeout(30000);
    }
}
