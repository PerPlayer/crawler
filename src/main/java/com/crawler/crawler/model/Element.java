package com.crawler.crawler.model;

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
public class Element {
    @Id
    @Column(name = "id", length = 32)
    private String id;
    @NotNull
    @Size(min = 5, max = 50, message = "{length.too.long}"/*, groups = ElementGroup.class*/)
    @Column(name = "title", length = 50)
    private String title;
//    @Pattern(regexp = "[0-9]*", message = "类型错误")
    @NotNull
    @Column(name = "content", length = 4000)
    private String content;
    @NotNull
    @Column(name = "taskId", length = 32)
    private String taskId;
    private Date createTime;
    private Date modifyTime;
    @Digits(integer = 2, fraction = 2, message = "超出范围")
    @Column(name = "weight", length = 1)
    private int weight;
    //    @Transient
    @Column(name = "status", length = 1)
    private int status;
    @Version
    private int version;

    public interface ElementGroup{}
}
