package com.crawler.util;

import lombok.Data;

import java.util.List;

@Data
public class BPage<T> {

    public static <T> BPage of(List<T> content) {
        BPage bPage = new BPage();
        bPage.setList(content);
        return bPage;
    }

    public static <T> BPage of(int page, int size) {
        BPage<T> bPage = new BPage<T>();
        bPage.setCurrent(page);
        bPage.setSize(size);
        return bPage;
    }

    private int size;
    private int current;
    private long total;
    private List<T> list;
}
