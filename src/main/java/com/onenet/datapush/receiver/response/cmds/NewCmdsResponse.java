package com.onenet.datapush.receiver.response.cmds;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewCmdsResponse {
	@JsonProperty(value = "cmd_uuid")
	public String cmduuid;

	public String getCmduuid() {
		return cmduuid;
	}

	public void setCmduuid(String cmduuid) {
		this.cmduuid = cmduuid;
	}

}
