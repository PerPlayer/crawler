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
@Entity(name = "crawler_menu")
public class MenuEntity extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 菜单ID
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 父菜单ID，一级菜单为0
	 */
	private String parentId;
	
	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：user:list,user:create)
	 */
	private String perms;

	/**
	 * 类型     0：目录   1：菜单   2：按钮
	 */
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;

	@Transient
	private List<MenuEntity> childs;
}
