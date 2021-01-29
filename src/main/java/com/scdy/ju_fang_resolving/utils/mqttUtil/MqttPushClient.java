package com.scdy.java.modules.hk_util.mqttUtil;

import com.scdy.ju_fang_resolving.utils.mqttUtil.MqttPushCallback;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


public class MqttPushClient {

	private static Properties properties = null;

	static {
		properties = new Properties();
		try {
			properties.load(MqttPushClient.class.getClassLoader().getResourceAsStream("./prop/mqtt_prop.properties"));
			MQTT_HOST = properties.getProperty("mqtt_url");
			MQTT_CLIENT_ID = properties.getProperty("mqtt_client_id");
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private static final Logger log = LoggerFactory.getLogger(MqttPushClient.class);
	private static String MQTT_HOST;
	private static String MQTT_CLIENT_ID;
	private static String MQTT_USERNAME = "";
	private static String MQTT_PASSWORD = "";
	private static int MQTT_TIMEOUT = 30;
	private static int MQTT_KEEPALIVE = 30;

	private MqttClient client;
	private static volatile MqttPushClient mqttClient = null;

	public static MqttPushClient getInstance() {
		if (mqttClient == null) {
			synchronized (MqttPushClient.class) {
				if (mqttClient == null) {
					mqttClient = new MqttPushClient();
				}
			}
		}
		return mqttClient;
	}

	private MqttPushClient() {
		log.info("Connect MQTT: " + this);
		connect();
	}

	private void connect() {
		try {
			client = new MqttClient(MQTT_HOST, MQTT_CLIENT_ID, new MemoryPersistence());
			MqttConnectOptions option = new MqttConnectOptions();
			option.setCleanSession(true);
//			option.setUserName(MQTT_USERNAME);
//			option.setPassword(MQTT_PASSWORD.toCharArray());
			option.setConnectionTimeout(MQTT_TIMEOUT);
			option.setKeepAliveInterval(MQTT_KEEPALIVE);
			option.setAutomaticReconnect(true);
			try {
				client.setCallback(new MqttPushCallback());
				client.connect(option);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发布主题，用于通知<br>
	 * 默认qos为1 非持久化
	 *
	 * @param topic
	 * @param data
	 */
	public void publish(String topic, String data) {
		publish(topic, data, 1, false);
	}

	/**
	 * 发布
	 *
	 * @param topic
	 * @param data
	 * @param qos
	 * @param retained
	 */
	public void publish(String topic, String data, int qos, boolean retained) {
		MqttMessage message = new MqttMessage();
		message.setQos(qos);
		message.setRetained(retained);
		message.setPayload(data.getBytes());
		MqttTopic mqttTopic = client.getTopic(topic);
		if (null == mqttTopic) {
			log.error("Topic Not Exist");
		}
		MqttDeliveryToken token;
		try {
			token = mqttTopic.publish(message);
			token.waitForCompletion();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 订阅某个主题 qos默认为1——send messages at least once
	 *
	 * @param topic
	 */
	public void subscribe(String topic) {
		subscribe(topic, 1);
	}

	/**
	 * 订阅某个主题
	 *
	 * @param topic
	 * @param qos
	 */
	public void subscribe(String topic, int qos) {
		try {
			client.subscribe(topic, qos);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
