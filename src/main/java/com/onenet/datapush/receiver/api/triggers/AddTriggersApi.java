package com.onenet.datapush.receiver.api.triggers;

import com.onenet.datapush.receiver.httpUtil.HttpPostMethod;
import com.onenet.datapush.receiver.model.Location;
import com.onenet.datapush.receiver.response.device.NewDeviceResponse;
import com.onenet.datapush.receiver.response.triggers.NewTriggersResponse;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.onenet.datapush.receiver.exception.OnenetApiException;
import com.onenet.datapush.receiver.httpUtil.HttpPutMethod;
import com.onenet.datapush.receiver.request.RequestInfo.Method;
import com.onenet.datapush.receiver.response.BasicResponse;
import com.onenet.datapush.receiver.triggers.AbstractAPI;
import com.onenet.datapush.receiver.utils.Config;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTriggersApi extends AbstractAPI{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String title;
	private String dsid;
	private List<String> devids;
	private List<String>dsuuids;
	private String desturl;
	private String type;
	private Integer threshold;
	private HttpPostMethod HttpMethod;
	
	
	/**
	 * @param title
	 * @param dsid
	 * @param devids
	 * @param dsuuids
	 * @param desturl
	 * @param type
	 * @param threshold
	 * @param key
	 */
	public AddTriggersApi(String title, String dsid, List<String> devids, List<String> dsuuids, String desturl,
			String type, Integer threshold,String key) {
		super();
		this.title = title;
		this.dsid = dsid;
		this.devids = devids;
		this.dsuuids = dsuuids;
		this.desturl = desturl;
		this.type = type;
		this.threshold = threshold;
		this.key=key;
		this.method = Method.POST;
        Map<String, Object> headmap = new HashMap<String, Object>();
        HttpMethod=  new HttpPostMethod(method);
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
        this.url=Config.getString("test.url")+"/triggers";
        // body参数
        Map<String, Object> bodymap = new HashMap<String, Object>();
        if (title != null) {
            bodymap.put("title", title);
        }
        if (dsid != null) {
            bodymap.put("ds_id", dsid);
        }
        if (devids != null) {
            bodymap.put("dev_ids", devids);
        }
        if (dsuuids != null) {
            bodymap.put("ds_uuids", dsuuids);
        }
        if (desturl != null) {
            bodymap.put("url", desturl);
        }
        if (type != null) {
            bodymap.put("type", type);
        }
        if (threshold != null) {
            bodymap.put("threshold", threshold);
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



	public BasicResponse<NewTriggersResponse> executeApi(){
		BasicResponse response=null;
		try {
			HttpResponse httpResponse=HttpMethod.execute();
			response = mapper.readValue(httpResponse.getEntity().getContent(),BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), NewTriggersResponse.class);
			response.setData(newData);
			return response;
		} catch (Exception e) {
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