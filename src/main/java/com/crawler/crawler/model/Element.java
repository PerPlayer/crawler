package com.crawler.crawler.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Element {
    @Id
    private long id;
    private String title;
    private String content;
    private long taskId;
    private Date creatTime;
    private Date modifyTime;
    private int weight;
    private int status;
}
