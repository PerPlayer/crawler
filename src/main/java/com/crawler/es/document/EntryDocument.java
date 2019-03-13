package com.crawler.es.document;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@Data
@Document(indexName = "crawler_entry", type = "crawler_entry")
public class EntryDocument {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("content")
    private String content;
    @JsonProperty("taskId")
    private String taskId;
    @JsonProperty("weight")
    private int weight;
    @JsonProperty("status")
    private int status;
    @JsonProperty("createTime")
    private Date createTime;
    @JsonProperty("modifyTime")
    private Date modifyTime;
}
