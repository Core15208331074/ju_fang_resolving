package com.scdy.ju_fang_resolving.controller;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.scdy.ju_fang_resolving.utils.mqttUtil.MqttPushClient;
import com.scdy.ju_fang_resolving.utils.udpUtil.UDPUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(description = "UDP测试")
@RestController
@RequestMapping("/testUDP")
public class TestUdpController {

    @GetMapping("/orderCheck")
    public String orderCheck(String order) {
        try {
            byte[] bytes = order.getBytes();
            UDPUtil.sendData(bytes);
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @ApiOperation("接收字符串数据")
    @GetMapping("/receiveStringData")
    public String receiveStringData() {
        try {
            UDPUtil.receiveStringData();
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @ApiOperation("接收十六进制数据")
    @GetMapping("/receiveHexadecimalData")
    public String receiveHexadecimalData() {
        try {
            String hexadecimalData = UDPUtil.receiveHexadecimalData();
            JSONObject object = UDPUtil.resolvingData(hexadecimalData);
            MqttPushClient.getInstance().publish("ju_fang_resolving", object.toJSONString());
            return hexadecimalData;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @ApiOperation("接收文件数据")
    @GetMapping("/receiveFileData")
    public String receiveFileData() {
        try {
            UDPUtil.receiveFileData();
            return "ok";
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

}
