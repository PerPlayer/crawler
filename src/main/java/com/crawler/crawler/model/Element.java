package com.crawler.crawler.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class Element {
    private long id;
    private String title;
    private String content;
    private long taskId;
    private Date creatTime;
    private Date modifyTime;
    private int weight;
    private int status;
}
