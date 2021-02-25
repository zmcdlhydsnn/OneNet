package com.onenet.datapush.receiver.api.device;

import com.onenet.datapush.receiver.httpUtil.HttpGetMethod;
import com.onenet.datapush.receiver.httpUtil.HttpPostMethod;
import com.onenet.datapush.receiver.model.Location;
import com.onenet.datapush.receiver.response.device.KeyRelDeviceList;
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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class FindKeyRelDeviceListApi extends AbstractAPI{
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private Integer page;
    private Integer perPage;
    private String deviceId;
    private String deviceTitle;
    private Boolean isRlated;
    private String apiKey;
    private HttpGetMethod HttpMethod;

    public FindKeyRelDeviceListApi(Integer page, Integer perPage, String deviceId,String deviceTitle, Boolean isRlated, String apiKey, String masterKey) {
        this.page = page;
        this.perPage = perPage;
        this.deviceId = deviceId;
        this.deviceTitle = deviceTitle;
        this.isRlated = isRlated;
        this.apiKey = apiKey;
        this.key = masterKey;
        this.method = Method.GET;
        this.HttpMethod=new HttpGetMethod(method);
        this.url = Config.getString("test.url") + "/keys/"+apiKey+"/devices" ;
        Map<String, Object> headmap = new HashMap<String, Object>();
        Map<String, Object> urlmap = new HashMap<String, Object>();
        if(page!=null){
            urlmap.put("page", page);
        }
        if(perPage!=null){
            urlmap.put("per_page", perPage);
        }
        if(StringUtils.isNotBlank(deviceId)){
            urlmap.put("device_id", deviceId);
        }
        if(StringUtils.isNotBlank(deviceTitle)){
            urlmap.put("device_title", deviceTitle);
        }
        if(isRlated!=null){
            urlmap.put("is_related", isRlated);
        }
        headmap.put("api-key", masterKey);
        HttpMethod.setHeader(headmap);
        HttpMethod.setcompleteUrl(url,urlmap);
    }


    public BasicResponse<KeyRelDeviceList> executeApi() {
        BasicResponse response;
//        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        try {
            HttpResponse httpResponse=HttpMethod.execute();
            response = mapper.readValue(httpResponse.getEntity().getContent(), BasicResponse.class);
            response.setJson(mapper.writeValueAsString(response));
            Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), KeyRelDeviceList.class);
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
