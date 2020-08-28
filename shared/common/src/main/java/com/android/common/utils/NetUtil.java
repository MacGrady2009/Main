package com.android.common.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

public class NetUtil {

    /**
     * 是否为wifi
     * @param context
     * @return
     */
    public boolean isWifi(Context context) {
        boolean result = false;
        NetworkCapabilities nc = getNC(context);
        if (null != nc){
            result =  nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
        }
        return result;
    }

    /**
     * 是否为手机网络
     * @param context
     * @return
     */
    public boolean isMobile(Context context) {
        boolean result = false;
        NetworkCapabilities nc = getNC(context);
        if (null != nc){
            result =  nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);
        }
        return result;
    }

    /**
     * 是否连接
     * @param context
     * @return
     */
    public boolean isConnected(Context context) {
        boolean result = false;
        NetworkCapabilities nc = getNC(context);
        if (null != nc){
            result =  nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return result;
    }

    /**
     * 是否可用
     * @param context
     * @return
     */
    public boolean isAvailable(Context context) {
        boolean result = false;
        NetworkCapabilities nc = getNC(context);
        if (null != nc){
            result =  nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }
        return result;
    }

    public int getDownloadSpeed(Context context){
        int result = 0;
        NetworkCapabilities nc = getNC(context);
        if (null != nc){
            result =  nc.getLinkDownstreamBandwidthKbps();
        }
        return result;
    }

    public int getUpSpeed(Context context){
        int result = 0;
        NetworkCapabilities nc = getNC(context);
        if (null != nc){
            result =  nc.getLinkUpstreamBandwidthKbps();
        }
        return result;
    }

    private void registerNetworkCallback(Context context,ConnectivityManager.NetworkCallback callback) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        cm.registerNetworkCallback(builder.build(), callback);
    }

    private void unregisterNetworkCallback(Context context, ConnectivityManager.NetworkCallback callback) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        cm.unregisterNetworkCallback(callback);
    }


        private NetworkCapabilities getNC(Context context){
        NetworkCapabilities capabilities = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (null != network){
            capabilities = cm.getNetworkCapabilities(network);
        }
        return capabilities;
    }



}

