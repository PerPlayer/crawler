package com.crawler.util;

import lombok.Data;

import java.util.List;

@Data
public class BPage<T> {
    private int size;
    private int current;
    private long total;
    private List<T> list;
}
