package com.onenet.datapush.receiver.httpUtil;

import java.util.Map;

import com.onenet.datapush.receiver.exception.OnenetApiException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpDeleteMethod extends BasicHttpMethod {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public HttpDeleteMethod(Method method) {
		super(method);
		// TODO Auto-generated constructor stub
	}

	public void setEntity(String json) {
		// TODO Auto-generated method stub

	}

	public void setEntity(Map<String, String> stringMap, Map<String, String> fileMap) {
		// TODO Auto-generated method stub

	}

	public void sethttpMethod(Method method) {
		// TODO Auto-generated method stub

	}

	public HttpResponse execute() throws Exception {
		HttpResponse httpResponse = null;
		httpClient = HttpClients.createDefault();
		httpResponse = httpClient.execute(httpRequestBase);
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if (statusCode != HttpStatus.SC_OK && statusCode != 221) {
			String response = EntityUtils.toString(httpResponse.getEntity());
			logger.error("request failed  status:{}, response::{}",statusCode, response);
			throw new OnenetApiException("request failed: " + response);
		}
		return httpResponse;

	}
}
