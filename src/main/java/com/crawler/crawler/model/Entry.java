package com.crawler.crawler.model;

import com.crawler.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class Entry extends BaseEntity {

    @Id
    @Column(name = "id", length = 32)
    private String id;
    @NotNull
    @Size(min = 5, max = 200, message = "{length.too.long}"/*, groups = EntryGroup.class*/)
    @Column(name = "title", length = 200)
    private String title;
    @NotNull
    @Column(name = "content", length = 4000)
    private String content;
    @NotNull
    @Column(name = "taskId", length = 32)
    private String taskId;
    @Digits(integer = 2, fraction = 2, message = "超出范围")
    @Column(name = "weight", length = 1)
    private int weight;

    public interface EntryGroup{}
}
