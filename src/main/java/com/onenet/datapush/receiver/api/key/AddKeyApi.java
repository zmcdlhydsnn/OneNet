package com.onenet.datapush.receiver.api.key;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onenet.datapush.receiver.exception.OnenetApiException;
import com.onenet.datapush.receiver.httpUtil.HttpPostMethod;
import com.onenet.datapush.receiver.model.Permissions;
import com.onenet.datapush.receiver.request.RequestInfo;
import com.onenet.datapush.receiver.response.BasicResponse;
import com.onenet.datapush.receiver.response.key.NewKeyResponse;
import com.onenet.datapush.receiver.triggers.AbstractAPI;
import com.onenet.datapush.receiver.utils.Config;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddKeyApi extends AbstractAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String title;
	private List<Permissions>permissions;
	private HttpPostMethod HttpMethod;
	/**
	 * @param title:名称（可选）,String
	 * @param permissions,List<Permissions>permissions
	 * @param key:masterkey 或者 该设备的设备key
	 */
	public AddKeyApi(String title, List<Permissions> permissions,String key) {
		this.title = title;
		this.permissions = permissions;
		this.key=key;
		this.method = RequestInfo.Method.POST;
        Map<String, Object> headmap = new HashMap<String, Object>();
        HttpMethod=  new HttpPostMethod(method);
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
        this.url= Config.getString("test.url")+"/keys";
        Map<String, Object> bodymap = new HashMap<String, Object>();
        if (title != null) {
            bodymap.put("title", title);
        }
        if(permissions!=null){
            bodymap.put("permissions", permissions);
        }
        String json=null;
        ObjectMapper remapper = new ObjectMapper();
        try {
            json = remapper.writeValueAsString(bodymap);
        } catch (Exception e) {
            logger.error("json error {}", e.getMessage());
            throw new OnenetApiException(e.getMessage());
        }
        ((HttpPostMethod)HttpMethod).setEntity(json);
        HttpMethod.setcompleteUrl(url,null);
	}

	public BasicResponse<NewKeyResponse> executeApi(){
		BasicResponse response=null;
		try {
			HttpResponse httpResponse=HttpMethod.execute();
			response = mapper.readValue(httpResponse.getEntity().getContent(),BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), NewKeyResponse.class);
			response.setData(newData);
			return response;
		} catch (Exception e) {
		e.printStackTrace();
			logger.error("json error {}", e.getMessage());
			throw new OnenetApiException(e.getMessage());
		}
		finally {
			try {
				HttpMethod.httpClient.close();
			} catch (Exception e) {
				logger.error("http close error: {}", e.getMessage());
				throw new OnenetApiException(e.getMessage());
			}
		}
	}
}
