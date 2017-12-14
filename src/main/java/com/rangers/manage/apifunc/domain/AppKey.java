package com.rangers.manage.apifunc.domain;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * APP应用凭证信息
 * 
 * @author vencano
 *
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = { @Index(columnList = "refToken") })
public class AppKey {

	@Id
	/* 分发的应用ID */
	private String appId;

	private String refToken;// 关联所属
	/* 密钥 */
	private String secretKey;
	/* 创建日期 */
	private Date createDate;
	/* 有效截止日期 */
	private Date validDate;
	/* 应用权限等级 */
	private String appGrade;
	/* 是否禁用 */
	private Boolean disabled;

	/* 拥有的api，及调用次数上限 */
	@Column(columnDefinition = "blob")
	private String apisJson;

	/* 拥有的api，及调用次数上限 <api名称,[<calltimes,调用次数><alltimes,调用上限>]> */
	@Transient
	private Map<String, Map<String, Integer>> apis;

	private final static ObjectMapper mapper = new ObjectMapper();

	public void setApis(Map<String, Map<String, Integer>> apis) {
		this.apis = apis;
		try {
			this.apisJson = CollectionUtils.isEmpty(apis) ? null : mapper.writeValueAsString(apis);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	public Map<String, Map<String, Integer>> getApis() {
		try {
			return this.apis = StringUtils.hasText(apisJson)
					? mapper.readValue(apisJson, new TypeReference<Map<String, Map<String, Integer>>>() {
					}) : null;
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
