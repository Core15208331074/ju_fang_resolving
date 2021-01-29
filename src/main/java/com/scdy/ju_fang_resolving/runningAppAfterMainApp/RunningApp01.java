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
    }

    public static void main(String[] args) {
        String word = "00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 78 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 00 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 00 00 00 00 00 00 00 14 c8 00 00 00 00 00 00 14 78 00 00 00 00 00 00 15 40 00 00 00 00 00 00 14 78 00 00 00 00 00 00 14 c8 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 15 18 00 00 00 00 00 00 15 40 00 00 00 00 00 00 14 c8 00 00 00 00 00 00 15 18 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 f0 00 00 00 00 00 00 15 e0 00 00 00 00 00 00 15 40 00 00 00 00 00 00 14 28 00 00 00 00 00 00 15 90 00 00 00 00 00 00 15 18 00 00 00 00 00 00 14 78 00 00 00 00 00 00 14 50 00 00 00 00 00 00 15 68 00 00 00 00 00 00 14 f0 00 00 00 00 00 00 14 f0 00 00 00 00 00 00 15 18 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 50 00 00 00 00 00 00 15 90 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 78 00 00 00 00 00 00 15 40 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 78 00 00 00 00 00 00 14 78 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 00 00 00 00 00 00 00 14 f0 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 13 d8 00 00 00 00 00 00 13 b0 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 28 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 28 00 00 00 00 00 00 14 00 00 00 00 00 00 00 14 28 00 00 00 00 00 00 14 f0 00 00 00 00 00 00 14 a0 00 00 00 00 00 00 14 50 00 00 00 00 00 00 14 78 00 00 00 00 00 00 48 42 00 00 00 c3 50";
        String[] array = word.split(" ");
        StringBuffer stringBuffer1 = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        for (int i = 134; i < array.length; i++) {
            stringBuffer1.append(array[i]);
            stringBuffer1.append(" ");
            if (i >= array.length - 7) {
                continue;
            } else {
                stringBuffer2.append(array[i]);
                stringBuffer2.append(" ");
            }

        }
        System.out.println(stringBuffer1.toString());
        System.out.println("###################################");
        System.out.println(stringBuffer2.toString().split(" ").length);
    }
}
