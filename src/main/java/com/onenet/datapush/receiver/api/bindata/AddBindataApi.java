package com.onenet.datapush.receiver.api.bindata;

import com.onenet.datapush.receiver.httpUtil.HttpPostMethod;
import com.onenet.datapush.receiver.model.Location;
import com.onenet.datapush.receiver.response.bindata.NewBindataResponse;
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

public class AddBindataApi extends AbstractAPI{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private  String devId;
	private  String datastreamId;
	private  String filename;
	private  String filepath;
	private HttpPostMethod HttpMethod;
	/**
	 * @param devId:数据所属设备（必选）,String
	 * @param datastreamId:该数据所属已经存在的数据流（必选）,String
	 * @param key:masterkey 或者 该设备的设备key
	 * @param filename:文件名,String
	 * @param filepath：路径,String
	 */
	public AddBindataApi(String devId, String datastreamId,String key, String filename,String filepath) {
		this.devId = devId;
		this.datastreamId = datastreamId;
		this.key=key;
		this.filename=filename;
		this.filepath=filepath;
		this.method=Method.POST;

        Map<String, Object> headmap = new HashMap<String, Object>();
        HttpMethod=  new HttpPostMethod(method);
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
        this.url=Config.getString("test.url")+"/bindata";
        Map<String, Object> urlmap = new HashMap<String, Object>();
        if(devId!=null){
            urlmap.put("device_id", devId);
        }
        if(datastreamId!=null){
            urlmap.put("datastream_id", datastreamId);
        }
        Map<String, String> fileMap=new HashMap<String, String>();
        fileMap.put(filename, filepath);
        ((HttpPostMethod)HttpMethod).setEntity(null,fileMap);
        HttpMethod.setcompleteUrl(url,urlmap);
	}

	public BasicResponse<NewBindataResponse> executeApi(){
		BasicResponse response=null;
		try {
			HttpResponse httpResponse=HttpMethod.execute();
			response = mapper.readValue(httpResponse.getEntity().getContent(),BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), NewBindataResponse.class);
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
