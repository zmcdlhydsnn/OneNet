package com.onenet.datapush.receiver.response.key;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NewKeyResponse {
    @JsonProperty(value="key")
    public String key;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}


}
