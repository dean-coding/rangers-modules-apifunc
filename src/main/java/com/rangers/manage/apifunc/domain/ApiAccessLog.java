package com.rangers.manage.apifunc.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ApiAccessLog {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	/* api名称 */
	private String apiName;
	
	private String host;
	/* 接口路径 */
	private String uri;
	/* 访问时间 */
	private Date accessDate;
	/* 请求参数 */
	private String reqParam;
	/* 返回参数 */
	private String resParam;
	/* 异常内容 */
	private String exp;

}
