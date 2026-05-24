package com.example.demo.plc;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * PCL 通信 服务端（监听本地端口）
 */
public class PclServer {
    public static void main(String[] args) {
        int port = 8888; // 本地模拟端口

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("=== PCL 服务端已启动，监听本地端口：" + port);

            // 等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("客户端已连接！");

            // 读取客户端发送的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String msg = br.readLine();

            System.out.println("【PCL 收到消息】：" + msg);

            // 关闭
            br.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}