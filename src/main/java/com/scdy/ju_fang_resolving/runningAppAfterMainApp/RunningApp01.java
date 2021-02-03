package com.scdy.ju_fang_resolving.runningAppAfterMainApp;

import com.scdy.ju_fang_resolving.utils.mqttUtil.MqttPushClient;
import com.scdy.ju_fang_resolving.utils.udpUtil.UDPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class RunningApp01 implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//        UDPUtil.receiveData();
        MqttPushClient.getInstance().subscribe("ju_fang_resolving");
        MqttPushClient.getInstance().subscribe("/v1/mount_e_protocol_conversion_web/devices/datas/up");
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            System.out.println(UUID.randomUUID().toString().replace("-", ""));
            System.out.println("##################################");
        }
    }
}
