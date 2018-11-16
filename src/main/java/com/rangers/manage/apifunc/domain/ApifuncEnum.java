package com.rangers.manage.apifunc.domain;

/**
 * API功能汇总
 * apifunc枚举<name,comment>-<api名称,api释义>
 * 
 * @author vencano
 *
 */
public enum ApifuncEnum {

	GET_MONTH_ORDERS("获取一月订单列表"), GET_YERAR_ORDERS("获取一年订单列表");

	private String comment;

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	private ApifuncEnum(String comment) {
		this.comment = comment;
	}

}
