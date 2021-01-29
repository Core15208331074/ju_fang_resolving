package com.scdy.ju_fang_resolving.test;

import lombok.SneakyThrows;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ReceiveData {

    @SneakyThrows
    public static void main(String[] args) {
        byte[] bytes = new byte[1024 * 10];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
        DatagramSocket receive = new DatagramSocket(9999);
        receive.receive(packet);
        String data = new String(packet.getData());
        System.out.println(data);
        receive.close();

//        DatagramChannel channel = DatagramChannel.open();
//        channel.socket().bind(new InetSocketAddress(9999));
//        ByteBuffer buf = ByteBuffer.allocate(48);
//        buf.clear();
//        channel.receive(buf);
//        buf.flip();
//        while (buf.hasRemaining()) {
//            System.out.println((char) buf.get());
//        }

    }


}
