package com.scdy.ju_fang_resolving.runningAppAfterMainApp;

import com.scdy.ju_fang_resolving.utils.udpUtil.UDPUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RunningApp01 implements ApplicationListener<ApplicationEvent> {

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
//        UDPUtil.receiveData();
    }
}