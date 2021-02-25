package com.onenet.datapush.receiver.controller;

import com.onenet.datapush.receiver.api.bindata.AddBindataApi;
import com.onenet.datapush.receiver.api.bindata.GetBindataApi;
import com.onenet.datapush.receiver.api.datapoints.AddDatapointsApi;
import com.onenet.datapush.receiver.api.datapoints.GetDatapointsListApi;
import com.onenet.datapush.receiver.api.datastreams.DeleteDatastreamsApi;
import com.onenet.datapush.receiver.api.device.AddDevicesApi;
import com.onenet.datapush.receiver.api.device.DeleteDeviceApi;
import com.onenet.datapush.receiver.api.device.FindDevicesListApi;
import com.onenet.datapush.receiver.response.BasicResponse;
import com.onenet.datapush.receiver.response.bindata.NewBindataResponse;
import com.onenet.datapush.receiver.response.datapoints.DatapointsList;
import com.onenet.datapush.receiver.response.device.DeviceList;
import com.onenet.datapush.receiver.response.device.NewDeviceResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/device")
@Api(value = "设备操作")
public class DeviceController {
private static final String key = "UFNOrJWc28xtwp=tDnk3O7SUKUA=";
    @ResponseBody
    @PostMapping(value="/addDevice")
    @ApiOperation(value = "添加设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name="title",value = "设备名",dataType="String",required = true),
            @ApiImplicitParam(name="authInfo",value = "设备唯一编号",dataType="String",required = true),
            @ApiImplicitParam(name="desc",value = "设备描述",dataType="String"),
            @ApiImplicitParam(name="protocol",value = "接入协议默认为HTTP",dataType="String"),
            @ApiImplicitParam(name="tags",value = "设备标签可为一个或多个逗号（英文）分隔",dataType="String"),
            @ApiImplicitParam(name="isPrivate",value = "设备私密性默认为ture",dataType="Boolean"),
    })
    public String addDevice(
            String title,
            String authInfo,
            String desc,
            String protocol,
            String tags,
            boolean isPrivate
    ){
        List<String> tagsList = null;
        if(null!=tags){
            String[] tagsArr = tags.split(",");
            tagsList = Arrays.asList(tagsArr);
        }
        AddDevicesApi api = new AddDevicesApi(title, protocol, desc, tagsList, null, isPrivate, authInfo, null, null, key);
        BasicResponse<NewDeviceResponse> response = api.executeApi();
        return response.getJson();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @ResponseBody
    @GetMapping(value="/getDevice")
    @ApiOperation(value = "查询设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name="keyWords",value = "匹配关键字",dataType="String"),
            @ApiImplicitParam(name="devId",value = "指定设备ID，多个用逗号分隔",dataType="String"),
//            @ApiImplicitParam(name="begin",value = "起始时间",dataType="Date"),
//            @ApiImplicitParam(name="end",value = "结束时间",dataType="Date"),
    })
    public String getDevice(
            String keyWords,
            String devId
//            Date begin,
//            Date end
    ) {
        FindDevicesListApi api = new FindDevicesListApi(keyWords, null, devId, null, null, null, null, null, null, null,
                null,null,key);
        BasicResponse<DeviceList> response = api.executeApi();
        return response.getJson();
    }

    @ResponseBody
    @DeleteMapping(value = "/delDev")
    @ApiOperation(value = "删除设备")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devId",value = "指定设备ID，多个用逗号分隔",dataType="String"),
    })
    public String delDev(
            String devId
    ){
        DeleteDeviceApi api = new DeleteDeviceApi(devId, key);
        BasicResponse<Void> response = api.executeApi();
        return response.getJson();
    }

    @ResponseBody
    @PostMapping(value = "/addData")
    @ApiOperation(value = "指定设备添加数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devId",value = "设备ID",dataType="String",required = true),
            @ApiImplicitParam(name="data",value = "数据信息类型为Map<String,List<Map<String,Object>>" +
                    "例如:{\"datastream_idxx\"{\"2021-02-24T17:53:12\":value}} 严格按照此时间格式" +
                    "",dataType="String",required = true),
    })
    public String addData(
            String devId,
            String data
    ){
        AddDatapointsApi api = new AddDatapointsApi(null, data, 4, devId, key);
        BasicResponse<Void> response = api.executeApi();
        return response.getJson();
    }

    @ResponseBody
    @GetMapping(value = "/getData")
    @ApiOperation(value = "获取设备数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devId",value = "设备ID",dataType="String",required = true),
            @ApiImplicitParam(name="limit",value = "数据量",dataType="int",required = true),
            @ApiImplicitParam(name="dataStreamIds",value = "数据名称逗号（英文）间隔",dataType="String"),
            @ApiImplicitParam(name="start",value = "开始时间yyyy-MM-ddTHH:mm:ss(严格按照此格式)",dataType="String"),
            @ApiImplicitParam(name="end",value = "结束时间yyyy-MMTdd-HH:mm:ss(严格按照此格式)",dataType="String"),
    })
    public String getData(
            String devId,
            String dataStreamIds,
            String start,
            String end,
            int limit
    ){
        GetDatapointsListApi api = new GetDatapointsListApi(dataStreamIds, start, end, devId, null, limit, null, null,
                null, null, null, key);
        BasicResponse<DatapointsList> response = api.executeApi();
        return response.getJson();
    }
    @ResponseBody
    @DeleteMapping(value = "/delData")
    @ApiOperation(value = "删除设备数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devId",value = "设备ID",dataType="String",required = true),
            @ApiImplicitParam(name="dsid",value = "数据流名称",dataType="String",required = true),
    })
    public String delData(
            String devId,
            String dsid
    ){
        DeleteDatastreamsApi api = new DeleteDatastreamsApi(devId, dsid, key);
        BasicResponse<Void> response = api.executeApi();
        return response.getJson();
    }


    @ResponseBody
    @PostMapping(value = "/addFile")
    @ApiOperation(value = "指定设备添加文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name="devId",value = "设备ID",dataType="String",required = true),
            @ApiImplicitParam(name="dataStreamId",value = "该数据所属已经存在的数据流",dataType="String",required = true),
            @ApiImplicitParam(name="filename",value = "文件名",dataType="String"),
            @ApiImplicitParam(name="filepath",value = "路径",dataType="String"),
    })
    public String addFile(
            String devId,
            String dataStreamId,
            String filename,
            String filepath
    ){
        AddBindataApi api = new AddBindataApi(devId, dataStreamId, key, filename, filepath);
        BasicResponse<NewBindataResponse> response = api.executeApi();
        return response.getJson();
    }

    @ResponseBody
    @GetMapping(value = "/getFile")
    @ApiOperation(value = "查询设备文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name="index",value = "数据索引号",dataType="String",required = true),
    })
    public String getFile(
            String index
    ){
        GetBindataApi api = new GetBindataApi(index, key);
        return api.executeApi();
    }

}
