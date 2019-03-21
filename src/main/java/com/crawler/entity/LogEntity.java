package com.crawler.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

/**
 * 菜单管理
 */
@Data
@Entity(name = "crawler_log")
public class LogEntity extends BaseEntity {

	@Id
	@Column(name = "id", length = 32)
	private String id;

	@Column(name = "username", length = 12)
	private String username;
	
	private String operation;

	private String method;

	private String params;

	private long time;

	private String ip;

}
