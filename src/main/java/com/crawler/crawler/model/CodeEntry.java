package com.crawler.crawler.model;

import com.crawler.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class CodeEntry extends BaseEntity {

    @Id
    @Column(name = "id", length = 32)
    private String id;

    private String code;
    private String category;
    private String value;
    private String description;
}
