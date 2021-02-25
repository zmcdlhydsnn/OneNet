package com.onenet.datapush.receiver.api.mqtt;

import com.onenet.datapush.receiver.httpUtil.HttpDeleteMethod;
import com.onenet.datapush.receiver.httpUtil.HttpPostMethod;
import com.onenet.datapush.receiver.model.Location;
import com.onenet.datapush.receiver.response.device.NewDeviceResponse;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.onenet.datapush.receiver.exception.OnenetApiException;
import com.onenet.datapush.receiver.httpUtil.HttpPutMethod;
import com.onenet.datapush.receiver.request.RequestInfo.Method;
import com.onenet.datapush.receiver.response.BasicResponse;
import com.onenet.datapush.receiver.triggers.AbstractAPI;
import com.onenet.datapush.receiver.utils.Config;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DeleteUserTopic extends AbstractAPI{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String name;
	private HttpDeleteMethod HttpMethod;
	/**
	 * 删除产品的Topic
	 * @param name：topic名（必选）
	 * @param key：masterkey
	 */
	public DeleteUserTopic(String name,String key) {
		this.name = name;
		this.key = key;
		this.method = Method.DELETE;
        Map<String, Object> headmap = new HashMap<String, Object>();
        Map<String, Object> urlmap = new HashMap<String, Object>();
        if(name!=null){
            urlmap.put("name", name);
        }
        HttpMethod = new HttpDeleteMethod(method);
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
        this.url = Config.getString("test.url") + "/mqtt" + "/topic" ;
        HttpMethod.setcompleteUrl(url,urlmap);

	}

	public BasicResponse<Void> executeApi() {
		BasicResponse response = null;
		try {
			HttpResponse httpResponse = HttpMethod.execute();
			response = mapper.readValue(httpResponse.getEntity().getContent(), BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			return response;
		} catch (Exception e) {
			logger.error("json error {}", e.getMessage());
			throw new OnenetApiException(e.getMessage());
		}finally {
			try {
				HttpMethod.httpClient.close();
			} catch (Exception e) {
				logger.error("http close error: {}", e.getMessage());
				throw new OnenetApiException(e.getMessage());
			}
		}
	
	}
}
