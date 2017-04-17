package com.gm.a80066158.gmtcptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.gm.a80066158.gmtcp.GMTCPServer;
import com.gm.a80066158.gmtcp.IOnRemoteDataReceiveListener;

public class MainActivity extends AppCompatActivity implements IOnRemoteDataReceiveListener {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initContent();
    }

    private void initContent() {
        Log.i(TAG, "<initContent> start");

        Button okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "<okButton:onClick> start");

                GMTCPServer tcpServer = new GMTCPServer(20000);
                tcpServer.setOnRemoteDataReceiveListener(MainActivity.this);
                tcpServer.start();
            }
        });
    }

    @Override
    public void onRemoteDataReceive(String remoteIp, int remotePort, String remoteData) {
        Log.i(TAG, "<onRemoteDataReceive> remoteIp = " + remoteIp + ", " +
            "remotePort = " + remotePort + ", remoteData = " + remoteData);
    }
}
