package com.gm.a80066158.gmtcpclienttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gm.a80066158.gmtcp.GMTCPClient;

import org.w3c.dom.Text;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private EditText ipEditText = null;
    private EditText portEditText = null;
    private TextView statusText = null;
    private Button connectButton = null;
    private EditText infoEditText = null;
    private Button sendButton = null;
    private GMTCPClient tcpClient = null;
    private boolean isConnect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");
        ipEditText = (EditText) findViewById(R.id.ipEditText);
        portEditText = (EditText) findViewById(R.id.portEditText);
        statusText = (TextView) findViewById(R.id.status);
        connectButton = (Button) findViewById(R.id.connectButton);
        infoEditText = (EditText) findViewById(R.id.info);
        sendButton = (Button) findViewById(R.id.sendButton);

        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onConnectButtonClick();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendButtonClick();
            }
        });
    }

    private void onConnectButtonClick() {
        Log.i(TAG, "<onConnectButtonClick> start");
        try {
            String serverIp = ipEditText.getText().toString();
            String serverPortStr = portEditText.getText().toString();
            int serverPort = Integer.parseInt(serverPortStr);
            Log.i(TAG, "<onConnectButtonClick> serverIp = " + serverIp + ", serverPort = " + serverPort);

            connectTcpServer(serverIp, serverPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onSendButtonClick() {
        Log.i(TAG, "<onSendButtonClick> start");

        String info = infoEditText.getText().toString();
        Log.i(TAG, "<onSendButtonClick> info = " + info);

        if (null == info) {
            return;
        }

        if (null == tcpClient) {
            return;
        }

        tcpClient.sendMsg(info);
    }

    private void connectTcpServer(final String serverIp, final int serverPort) {
        Log.i(TAG, "<connectTcpServer> serverIp = " + serverIp + ", serverPort = " + serverPort);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (false == isConnect) {
                    try {
                        tcpClient = new GMTCPClient(serverIp, serverPort);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("CONNECTED");
                                connectButton.setText("DISCONNECT");
                            }
                        });
                        isConnect = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                statusText.setText("CONNECT FAILED");
                                connectButton.setText("CONNECT");
                            }
                        });
                        isConnect = false;
                    }
                } else {
                    if (null != tcpClient) {
                        tcpClient.close();
                        tcpClient = null;
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            statusText.setText("NOT CONNECT");
                            connectButton.setText("CONNECT");
                        }
                    });

                    isConnect = false;
                }
            }
        }).start();
    }
}
