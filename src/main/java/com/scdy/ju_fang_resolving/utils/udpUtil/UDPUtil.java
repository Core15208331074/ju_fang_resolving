package com.scdy.ju_fang_resolving.utils.udpUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.scdy.ju_fang_resolving.utils.commonUtil.CommonUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Properties;

/**
 * UDP通道传输工具
 */
@Slf4j
@Data
public class UDPUtil {

    private static Properties properties;

    private static String host_listening;
    private static String port_listening;
    private static String host_target;
    private static String port_target;

    static {
        properties = new Properties();
        try {
            properties.load(UDPUtil.class.getClassLoader().getResourceAsStream("prop/udp_prop/server_prop.properties"));
            host_listening = properties.getProperty("host_listening");
            port_listening = properties.getProperty("port_listening");
            host_target = properties.getProperty("host_target");
            port_target = properties.getProperty("port_target");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 发送数据
     */
    @SneakyThrows
    public static void sendData(byte[] bytes) {
        InetAddress net = InetAddress.getByName(host_target);
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length, net, Integer.valueOf(port_target));
        DatagramSocket socket = new DatagramSocket();
        socket.send(packet);
        socket.close();

//        DatagramChannel channel = DatagramChannel.open();
//        ByteBuffer buf = ByteBuffer.allocate(48);
//        buf.clear();
//        buf.put("动物标本比较不错，开始行动吧！".getBytes());
//        buf.flip();
//        channel.send(buf, new InetSocketAddress(host_target, Integer.valueOf(port_target)));
    }

    /**
     * 接收字符串数据
     */
    @SneakyThrows
    public static String receiveStringData() {
        byte[] bytes = new byte[1024 * 2];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        DatagramSocket receive = new DatagramSocket(Integer.valueOf(port_listening));
        receive.receive(packet);
        String data = new String(packet.getData(), "utf-8").trim();
        System.out.println(data);
        receive.close();
        return data;


//        DatagramChannel channel = DatagramChannel.open();
//        channel.socket().bind(new InetSocketAddress(Integer.valueOf(port_listening)));
//        ByteBuffer buf = ByteBuffer.allocate(48);
//        buf.clear();
//        channel.receive(buf);
//        buf.flip();
//        while (buf.hasRemaining()) {
//            System.out.println((char) buf.get());
//        }
//        return buf.toString();
    }

    /**
     * 接收文件数据
     */
    @SneakyThrows
    public static void receiveFileData() {
        byte[] bytes = new byte[1024 * 2];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        DatagramSocket receive = new DatagramSocket(Integer.valueOf(port_listening));
        receive.receive(packet);
        byte[] fileBytes = packet.getData();
        receive.close();
        FileOutputStream out = new FileOutputStream(CommonUtil.getFile());
        BufferedOutputStream bOut = new BufferedOutputStream(out);
        bOut.write(fileBytes, 0, fileBytes.length);
        out.flush();
        bOut.flush();
        out.close();
        bOut.close();
        return;


//        DatagramChannel channel = DatagramChannel.open();
//        channel.socket().bind(new InetSocketAddress(Integer.valueOf(port_listening)));
//        ByteBuffer buf = ByteBuffer.allocate(48);
//        buf.clear();
//        channel.receive(buf);
//        buf.flip();
//        while (buf.hasRemaining()) {
//            System.out.println((char) buf.get());
//        }
//        return buf.toString();
    }


    /**
     * 接收十六进制数据
     */
    @SneakyThrows
    public static String receiveHexadecimalData() {
        byte[] bytes = new byte[1024 * 2];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        DatagramSocket receive = new DatagramSocket(Integer.valueOf(port_listening));
        receive.receive(packet);
        StringBuffer stringBuffer = new StringBuffer();
        byte[] data = packet.getData();
        ByteArrayInputStream byteInput = new ByteArrayInputStream(data);
        int byNum = -1;
        while ((byNum = byteInput.read()) != -1) {
            String hexadecimal = Integer.toHexString(byNum);
            if ("0".equals(hexadecimal)) {
                stringBuffer.append("00");
            } else {
                stringBuffer.append(hexadecimal);
            }
            stringBuffer.append(" ");
        }
        byteInput.close();
        stringBuffer.deleteCharAt(stringBuffer.lastIndexOf(" "));
        log.info(stringBuffer.toString().trim());
        receive.close();
        return stringBuffer.toString().trim();


//        DatagramChannel channel = DatagramChannel.open();
//        channel.socket().bind(new InetSocketAddress(Integer.valueOf(port_listening)));
//        ByteBuffer buf = ByteBuffer.allocate(48);
//        buf.clear();
//        channel.receive(buf);
//        buf.flip();
//        while (buf.hasRemaining()) {
//            System.out.println((char) buf.get());
//        }
//        return buf.toString();
    }


    /**
     * 解析十六进制数据
     */
    public static void resolvingData(String hexadecimalData) {
        JSONObject object = new JSONObject();
        String[] dataArray = hexadecimalData.split(" ");
        if (CommonUtil.isNotNull(dataArray) && dataArray.length > 0) {
            StringBuffer buffer = new StringBuffer();
            //取出头
            for (int i = 0; i < dataArray.length; i++) {
                buffer.append(dataArray[i]);
                if (i == 5) {
                    break;
                }
            }
            JSONArray channel = new JSONArray();
            object.put("head", buffer.toString());
        } else {
            object.put("head", null);
        }

    }

}
