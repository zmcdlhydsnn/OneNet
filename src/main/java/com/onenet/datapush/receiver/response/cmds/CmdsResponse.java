package com.onenet.datapush.receiver.response.cmds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CmdsResponse {
	@JsonProperty("status")
	private Integer status;
	@JsonProperty("desc")
	private String desc;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
}
