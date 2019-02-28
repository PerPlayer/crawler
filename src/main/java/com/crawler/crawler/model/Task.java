package com.crawler.crawler.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Task {
    private long id;
    private String href;
    private String source;
    private String domain;
    private Task childTask;
    private int weight;
    private int deep;
    private int status;
}
