package com.rangers.manage.apifunc.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * API详情信息 
 * @author vencano
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ApiInfo {

	/* 接口名称 */
	@Id
	private String name;
	/* 接口地址 */
	private String uri;
	
	private String comment;//接口释义
	/*
	 * http请求方式
	 */
	private String reqMethod;
	/* 每天调用次数上限 */
	private Integer accessLimit;
	/* 版本号 */
	private String version;
	/* 是否可用 */
	private boolean disabled;

	private Date createDate;

	public ApiInfo(String name, String uri, Integer accessLimit, String version, boolean disabled, Date createDate) {
		super();
		this.name = name;
		this.uri = uri;
		this.accessLimit = accessLimit;
		this.version = version;
		this.disabled = disabled;
		this.createDate = createDate;
	}

}
