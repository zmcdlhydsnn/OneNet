package com.onenet.datapush.receiver.api.dtu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onenet.datapush.receiver.httpUtil.HttpPostMethod;
import com.onenet.datapush.receiver.response.dtu.NewParserResponse;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.onenet.datapush.receiver.triggers.AbstractAPI;
import com.onenet.datapush.receiver.exception.OnenetApiException;
import com.onenet.datapush.receiver.request.RequestInfo.Method;
import com.onenet.datapush.receiver.response.BasicResponse;
import com.onenet.datapush.receiver.utils.Config;
import java.util.HashMap;
import java.util.Map;

public class AddDtuParser extends AbstractAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private String name;
	private String filepath;
	private HttpPostMethod HttpMethod;
	
	/**
	 * TCP透传新增
	 * @param name ： 名字,String
	 * @param filepath:路径，String
	 * @param key :必须为masterkey
	 */
	public AddDtuParser(String name, String filepath, String key) {
		super();
		this.name = name;
		this.filepath = filepath;
		this.key = key;
		this.method = Method.POST;
		Map<String, Object> headmap = new HashMap<String, Object>();
		HttpMethod = new HttpPostMethod(method);
		headmap.put("api-key", key);
		HttpMethod.setHeader(headmap);
		this.url = Config.getString("test.url") + "/dtu/parser";
		Map<String, String> fileMap = new HashMap<String, String>();
		fileMap.put("parser", filepath);
		Map<String, String> stringMap = new HashMap<String, String>();
		stringMap.put("name", name);
		((HttpPostMethod) HttpMethod).setEntity(stringMap, fileMap);
		HttpMethod.setcompleteUrl(url, null);
	}
	public BasicResponse<NewParserResponse> executeApi(){
		ObjectMapper mapper = new ObjectMapper();
		BasicResponse response=null;
		try {
			HttpResponse httpResponse=HttpMethod.execute();
			response = mapper.readValue(httpResponse.getEntity().getContent(),BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), NewParserResponse.class);
			response.setData(newData);
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
