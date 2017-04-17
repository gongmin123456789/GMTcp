package com.gm.a80066158.gmtcp;

/**
 * Created by 80066158 on 2017-04-14.
 */

public interface IOnRemoteDataReceiveListener {
    public void onRemoteDataReceive(String remoteIp, int remotePort, String remoteData);
}
