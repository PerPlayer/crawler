package com.crawler.crawler.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Task {
    @Id
    @Column(name = "id", length = 32)
    private String id;
    private String href;
    private String source;
    private String domain;
    @OneToOne
    @JoinColumn(name = "child_id")
    private Task childTask;
    private Date createTime;
    private Date modifyTime;
    @Column(name = "weight", length = 1)
    private int weight;
    @Column(name = "deep", length = 2)
    private int deep;
    @Column(name = "status", length = 1)
    private int status;
}
