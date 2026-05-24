package com.example.demo.plc;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * PCL 通信 客户端（发送数据到本地服务端）
 */
public class PclClient {
    public static void main(String[] args) {
        String ip = "127.0.0.1"; // 本地
        int port = 8888;

        try (Socket socket = new Socket(ip, port)) {
            // 发送数据
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write("Hello PCL 本地通信测试！\n");
            osw.flush();

            System.out.println("【PCL 发送成功】");
            osw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}