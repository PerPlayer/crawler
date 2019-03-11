package com.crawler.crawler.model;

import com.crawler.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "crawler_task")
@Data
@NoArgsConstructor
public class Task extends BaseEntity {

    @Id
    @Column(name = "id", length = 32)
    private String id;
    private String href;
    private String siteId;
    private String domain;
    private String description;
    @OneToOne
    @JoinColumn(name = "parent_id")
    private Task parentTask;
    @Column(name = "weight", length = 1)
    private int weight;
    @Column(name = "deep", length = 2)
    private int deep;
    @Column(name = "status", length = 1)
    private int status;
}
