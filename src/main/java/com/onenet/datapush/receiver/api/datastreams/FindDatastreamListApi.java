package com.onenet.datapush.receiver.api.datastreams;

import com.onenet.datapush.receiver.httpUtil.HttpGetMethod;
import com.onenet.datapush.receiver.httpUtil.HttpPostMethod;
import com.onenet.datapush.receiver.model.Location;
import com.onenet.datapush.receiver.response.datastreams.DatastreamsResponse;
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
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDatastreamListApi extends AbstractAPI {
	private HttpGetMethod HttpMethod;
	private String datastreamids;
	private String devId;
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	/**
	 * 查询多个数据流
	 * @param datastreamids:数据流名称 ,String
	 * @param devId:设备ID,String
	 * @param key:masterkey 或者 设备apikey
	 */
	public FindDatastreamListApi(String datastreamids, String devId, String key) {
		this.datastreamids = datastreamids;
		this.devId = devId;
		this.key = key;
		this.method = Method.GET;
		this.HttpMethod = new HttpGetMethod(method);
        Map<String, Object> headmap = new HashMap<String, Object>();
        Map<String, Object> urlmap = new HashMap<String, Object>();
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
        this.url = Config.getString("test.url") + "/devices/" + devId + "/datastreams";
        // url参数
        if (datastreamids != null) {
            urlmap.put("datastream_ids", datastreamids);
        }
        HttpMethod.setcompleteUrl(url, urlmap);
	}



	public BasicResponse<List<DatastreamsResponse>> executeApi() {
		BasicResponse response;
		/*ObjectMapper mapper = new ObjectMapper();
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));*/
		try {
			HttpResponse httpResponse = HttpMethod.execute();
			response = mapper.readValue(httpResponse.getEntity().getContent(), BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()),  new TypeReference<List<DatastreamsResponse>>(){});
			response.setData(newData);
			return response;
		} catch (Exception e) {
			logger.error("error: {}" , e.getMessage());
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
