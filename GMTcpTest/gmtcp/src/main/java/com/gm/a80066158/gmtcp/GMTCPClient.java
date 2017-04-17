package com.gm.a80066158.gmtcp;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by 80066158 on 2017-04-17.
 */

public class GMTCPClient {
    private static final String TAG = "GMTCPClient";

    private String serverIp = null;
    private int serverPort = 0;
    private Socket socket = null;

    public GMTCPClient(String serverIp, int serverPort) throws IOException {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        socket = new Socket(serverIp, serverPort);
    }

    public void sendMsg(String msg) {
        Log.i(TAG, "<sendMsg> msg = " + msg);
        if (null == msg) {
            return;
        }

        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(msg.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        Log.i(TAG, "<close> start");
        if (null == socket) {
            return;
        }

        try {
            socket.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        socket = null;
    }
}
