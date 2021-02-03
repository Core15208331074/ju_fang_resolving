package com.scdy.ju_fang_resolving.utils.udpUtil;

import com.alibaba.fastjson.JSONObject;
import com.scdy.ju_fang_resolving.utils.commonUtil.CommonUtil;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

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

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(UUID.randomUUID().toString().replace("-", ""));
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
        byte[] bytes = new byte[1050];
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
    public static JSONObject resolvingData(String hexadecimalData) throws InterruptedException {
        String[] dataArray = hexadecimalData.split(" ");
        if (CommonUtil.isNotNull(dataArray) && dataArray.length > 0) {
            //帧头
            StringBuffer header = new StringBuffer();
            //通道数据
            StringBuffer channel = new StringBuffer();
            //每一通道判断内容
            StringBuffer channelJudge = new StringBuffer();
            //厂家固定数据
            StringBuffer constantOfFactory = new StringBuffer();
            //外部参考电流频率
            StringBuffer frequencyOfElecCurr = new StringBuffer();
            //其他数据帧值
            StringBuffer other = new StringBuffer();
            for (int i = 0; i < dataArray.length; i++) {
                if (i <= 5) {//帧头
                    header.append(dataArray[i]);
                    header.append(" ");
                } else if (i >= 6 && i <= 133) {//通道数据
                    channel.append(dataArray[i]);
                    channel.append(" ");
                } else if (i >= 518 && i <= 541) {
                    channelJudge.append(dataArray[i]);//通道内容判断数据
                    channelJudge.append(" ");
                } else if (i >= 542 && i <= 546) {//厂家固定数据
                    constantOfFactory.append(dataArray[i]);
                    constantOfFactory.append(" ");
                } else if (i >= 547 && i <= 548) {//外部参考电流频率
                    frequencyOfElecCurr.append(dataArray[i]);
                    frequencyOfElecCurr.append(" ");
                } else if (i >= 549) {
                    continue;
                } else {
                    other.append(dataArray[i]);
                    other.append(" ");
                }
            }
            JSONObject data = new JSONObject();
            data.put("header", header.toString().substring(0, header.toString().lastIndexOf(" ")));
            data.put("constantOfFactory", constantOfFactory.toString().substring(0, constantOfFactory.toString().lastIndexOf(" ")));
            data.put("frequencyOfElecCurr", frequencyOfElecCurr.toString().substring(0, frequencyOfElecCurr.toString().lastIndexOf(" ")));
            data.put("other", other.toString().substring(0, other.toString().lastIndexOf(" ")));

            //将通道数据保存到文件
            composeChannelFrame(channel.toString().split(" "));
            data.put("channelJudge", composeChannelJudgeFrame(channelJudge.toString().split(" ")));
            return data;
        } else {
            return null;
        }
    }


    @SneakyThrows
    private static void composeChannelFrame(String[] frames) {
        log.info(String.valueOf(frames.length));
        if (CommonUtil.isNotNull(frames) && frames.length % 8 == 0) {
            StringBuffer channel01 = new StringBuffer();
            StringBuffer channel02 = new StringBuffer();
            StringBuffer channel03 = new StringBuffer();
            StringBuffer channel04 = new StringBuffer();
            for (int i = 0; i < frames.length; i++) {
                if (i % 8 == 0 || i % 8 == 1) {
                    channel01.append(frames[i]);
                    channel01.append(" ");
                }
                if (i % 8 == 2 || i % 8 == 3) {
                    channel02.append(frames[i]);
                    channel02.append(" ");
                }
                if (i % 8 == 4 || i % 8 == 5) {
                    channel03.append(frames[i]);
                    channel03.append(" ");
                }
                if (i % 8 == 6 || i % 8 == 7) {
                    channel04.append(frames[i]);
                    channel04.append(" ");
                }
            }
            byte[] chan01 = channel01.toString().getBytes();
            OutputStream out01 = new FileOutputStream(CommonUtil.getFile("channel01"));
            BufferedOutputStream bOut01 = new BufferedOutputStream(out01);
            bOut01.write(chan01, 0, chan01.length);
            out01.flush();
            bOut01.flush();
            out01.close();
            bOut01.close();

            byte[] chan02 = channel02.toString().getBytes();
            OutputStream out02 = new FileOutputStream(CommonUtil.getFile("channel02"));
            BufferedOutputStream bOut02 = new BufferedOutputStream(out02);
            bOut02.write(chan02, 0, chan02.length);
            out02.flush();
            bOut02.flush();
            out02.close();
            bOut02.close();

            byte[] chan03 = channel03.toString().getBytes();
            OutputStream out03 = new FileOutputStream(CommonUtil.getFile("channel03"));
            BufferedOutputStream bOut03 = new BufferedOutputStream(out03);
            bOut03.write(chan03, 0, chan01.length);
            out03.flush();
            bOut03.flush();
            out03.close();
            bOut03.close();

            byte[] chan04 = channel04.toString().getBytes();
            OutputStream out04 = new FileOutputStream(CommonUtil.getFile("channel04"));
            BufferedOutputStream bOut04 = new BufferedOutputStream(out04);
            bOut04.write(chan04, 0, chan04.length);
            out04.flush();
            bOut04.flush();
            out04.close();
            bOut04.close();
        }
    }


    @SneakyThrows
    private static Map<String, String> composeChannelJudgeFrame(String[] frames) {
        log.info(String.valueOf(frames.length));
        if (CommonUtil.isNotNull(frames) && frames.length % 6 == 0) {
            StringBuffer eventHappenBuffer = new StringBuffer();
            StringBuffer dischargeTypeBuffer = new StringBuffer();
            StringBuffer dischargeDiagnosisBuffer = new StringBuffer();
            StringBuffer phaseBuffer = new StringBuffer();
            StringBuffer avgBuffer = new StringBuffer();
            StringBuffer maxBuffer = new StringBuffer();
            for (int i = 0; i < frames.length; i++) {
                if (i % 6 == 0) {
                    eventHappenBuffer.append(frames[i]);
                    eventHappenBuffer.append(" ");
                }
                if (i % 6 == 1) {
                    dischargeTypeBuffer.append(frames[i]);
                    dischargeTypeBuffer.append(" ");
                }
                if (i % 6 == 2) {
                    dischargeDiagnosisBuffer.append(frames[i]);
                    dischargeDiagnosisBuffer.append(" ");
                }
                if (i % 6 == 3) {
                    phaseBuffer.append(frames[i]);
                    phaseBuffer.append(" ");
                }
                if (i % 6 == 4) {
                    avgBuffer.append(frames[i]);
                    avgBuffer.append(" ");
                }
                if (i % 6 == 5) {
                    maxBuffer.append(frames[i]);
                    maxBuffer.append(" ");
                }
            }
            String eventHappen = eventHappenBuffer.toString();
            String dischargeType = dischargeTypeBuffer.toString();
            String dischargeDiagnosis = dischargeDiagnosisBuffer.toString();
            String phase = phaseBuffer.toString();
            String avg = avgBuffer.toString();
            String max = maxBuffer.toString();
            Map<String, String> map = new HashMap<>();
            map.put("eventHappen", eventHappen.substring(0, eventHappen.lastIndexOf(" ")));
            map.put("dischargeType", dischargeType.substring(0, dischargeType.lastIndexOf(" ")));
            map.put("dischargeDiagnosis", dischargeDiagnosis.substring(0, dischargeDiagnosis.lastIndexOf(" ")));
            map.put("phase", phase.substring(0, phase.lastIndexOf(" ")));
            map.put("avg", avg.substring(0, avg.lastIndexOf(" ")));
            map.put("max", max.substring(0, max.lastIndexOf(" ")));
            return map;
        }
        return null;
    }
}
