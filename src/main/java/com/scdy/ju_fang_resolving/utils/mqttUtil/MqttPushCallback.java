package com.scdy.ju_fang_resolving.utils.mqttUtil;

import com.alibaba.fastjson.JSONObject;
import com.scdy.ju_fang_resolving.utils.commonUtil.CommonUtil;
import lombok.SneakyThrows;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Properties;


public class MqttPushCallback implements MqttCallback {
	private static final Logger log = LoggerFactory.getLogger(MqttPushCallback.class);

	private RestTemplate restTemplate = new RestTemplate();


	@Override
	public void connectionLost(Throwable cause) {
		log.info("断开连接，建议重连:" + cause.getMessage());
		log.info("断开连接，建议重连:" + cause.getLocalizedMessage());
		log.info("断开连接，建议重连:" + cause.getStackTrace());
		log.info("断开连接，建议重连:" + cause.getSuppressed());
		log.info("断开连接，建议重连:" + cause.getCause());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		log.info(token.isComplete() + "");
	}


//	@SneakyThrows
//	@Override
//	public void messageArrived(String topic, MqttMessage message) {
//		try {
//			String localURL = CommonUtil.getLocalURL();
//			JSONObject payload = (JSONObject) JSONObject.parse(new String(message.getPayload()));
//			JSONObject packet = (JSONObject) payload.get("packet");
//			JSONObject service = (JSONObject) packet.get("service");
//			String name = (String) service.get("name");
//			if ("Get".equals(name) || !CommonUtil.isNotNull(name)) {
//				ResponseEntity<JSONObject> entity = restTemplate.getForEntity(localURL + topic, JSONObject.class);
//				log.info(entity.toString());
//			} else if ("Post".equals(name)) {
//				Object description = service.get("description");
//				if (description instanceof String) {
//					Object parse = JSONObject.parse((String) description);
//					JSONObject jsonObject = restTemplate.postForObject(localURL + topic, parse, JSONObject.class);
//					log.info(jsonObject.toString());
//				} else {
//					JSONObject jsonObject = restTemplate.postForObject(localURL + topic, description, JSONObject.class);
//					log.info(jsonObject.toString());
//				}
//			} else if ("Put".equals(name)) {
//				Object description = service.get("description");
//				if (description instanceof String) {
//					Object parse = JSONObject.parse((String) description);
//					restTemplate.put(localURL + topic, parse);
//				} else {
//					restTemplate.put(localURL + topic, description);
//				}
//			} else if ("Delete".equals(name)) {
//				restTemplate.delete(localURL + topic);
//			}
//			log.info("Topic: " + topic);
//			log.info("Message: " + new String(message.getPayload()));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	private static Properties topicProperties;
	private static String devicesDataDown;

	private static Properties orderProperties;

	static {
		topicProperties = new Properties();
		orderProperties = new Properties();
		try {
			//加载主题的配置
			topicProperties.load(MqttPushCallback.class.getClassLoader().getResourceAsStream("./prop/mqtt_prop.properties"));
			devicesDataDown = (String) topicProperties.get("devices_data_down");
			//加载命令的配置
			orderProperties.load(MqttPushCallback.class.getClassLoader().getResourceAsStream("./prop/order_prop.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@SneakyThrows
	@Override
	public void messageArrived(String topic, MqttMessage message) {
		try {
			String localURL = CommonUtil.getLocalURL();
			if (CommonUtil.isNotNull(topic) && topic.equals(devicesDataDown)) {
				JSONObject payload = (JSONObject) JSONObject.parse(new String(message.getPayload()));
				JSONObject downPacket = (JSONObject) payload.get("packet");
				JSONObject cmdInfo = (JSONObject) downPacket.get("cmd");
				int operation = (int) cmdInfo.get("operation");
				String option = String.valueOf(operation);
				String url = (String) orderProperties.get(option);
				ResponseEntity<JSONObject> entity = restTemplate.getForEntity(localURL + url, JSONObject.class);
				log.info(entity.toString());
			} else {
//				JSONObject payload = (JSONObject) JSONObject.parse(new String(message.getPayload()));
//				JSONObject packet = (JSONObject) payload.get("packet");
//				JSONObject service = (JSONObject) packet.get("service");
//				String name = (String) service.get("name");
//				if ("Get".equals(name) || !CommonUtil.isNotNull(name)) {
//					ResponseEntity<JSONObject> entity = restTemplate.getForEntity(localURL + topic, JSONObject.class);
//					log.info(entity.toString());
//				} else if ("Post".equals(name)) {
//					Object description = service.get("description");
//					if (description instanceof String) {
//						Object parse = JSONObject.parse((String) description);
//						JSONObject jsonObject = restTemplate.postForObject(localURL + topic, parse, JSONObject.class);
//						log.info(jsonObject.toString());
//					} else {
//						JSONObject jsonObject = restTemplate.postForObject(localURL + topic, description, JSONObject.class);
//						log.info(jsonObject.toString());
//					}
//				} else if ("Put".equals(name)) {
//					Object description = service.get("description");
//					if (description instanceof String) {
//						Object parse = JSONObject.parse((String) description);
//						restTemplate.put(localURL + topic, parse);
//					} else {
//						restTemplate.put(localURL + topic, description);
//					}
//				} else if ("Delete".equals(name)) {
//					restTemplate.delete(localURL + topic);
//				}
			}
			log.info("Topic: " + topic);
			log.info("Message: " + new String(message.getPayload()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
