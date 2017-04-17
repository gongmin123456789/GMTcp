package com.gm.a80066158.gmtcp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by 80066158 on 2017-04-14.
 */

public class GMTCPServer {
    private static final String TAG = "GMTCPServer";

    private static final int BUF_MAX_LENGTH = 1260;

    private int port = 0;
    private IOnRemoteDataReceiveListener onRemoteDataReceiveListener = null;
    private ServerSocket serverSocket = null;
    private boolean isStarted = false;

    public GMTCPServer(int port) {
        this.port = port;
    }

    public void setOnRemoteDataReceiveListener(IOnRemoteDataReceiveListener onRemoteDataReceiveListener) {
        this.onRemoteDataReceiveListener = onRemoteDataReceiveListener;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public synchronized void start() {
        Log.i(TAG, "<start> isStarted = " + isStarted);

        if (isStarted) {
            return;
        }
        isStarted = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                createServerSocket();
            }
        }).start();
    }

    private void createServerSocket() {
        Log.i(TAG, "<createServerSocket> start");
        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                final Socket clientSocket = serverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        onNewClientConnect(clientSocket);
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void onNewClientConnect(Socket clientSocket) {
        Log.i(TAG, "<onNewClientConnect> start");

        InputStream inputStream = null;
        try {
            inputStream = clientSocket.getInputStream();
            int readLen = 0;
            byte[] buf = new byte[BUF_MAX_LENGTH];

            while (true) {
                readLen = inputStream.read(buf, 0, BUF_MAX_LENGTH);
                Log.i(TAG, "<onNewClientConnect> readLen = " + readLen);

                if (null != onRemoteDataReceiveListener) {
                    onRemoteDataReceiveListener.onRemoteDataReceive(clientSocket.getInetAddress().getHostName(),
                            clientSocket.getPort(), new String(buf, 0, readLen));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            inputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
