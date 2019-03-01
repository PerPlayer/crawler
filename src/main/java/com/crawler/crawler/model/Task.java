package com.crawler.crawler.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Task {
    @Id
    private long id;
    private String href;
    private String source;
    private String domain;
    @OneToOne
    @JoinColumn(name = "child_id")
    private Task childTask;
    private int weight;
    private int deep;
    private int status;
}
