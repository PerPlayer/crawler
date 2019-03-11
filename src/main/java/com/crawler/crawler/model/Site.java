package com.crawler.crawler.model;

import com.crawler.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Version;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity(name = "crawler_site")
@Data
@NoArgsConstructor
public class Site extends BaseEntity {

    @Id
    @Column(name = "id", length = 32)
    private String id;
    @Column(name = "site_name", length = 50)
    private String siteName;
    @NotNull
    @Size(min = 5, max = 50, message = "{length.too.long}")
    @Column(name = "domain", length = 50)
    private String domain;
    @NotNull
    @Column(name = "robots", length = 1024)
    private String robots;

}
