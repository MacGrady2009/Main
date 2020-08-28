package com.android.common.base;

import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import androidx.annotation.NonNull;


public class BaseNetworkCallback extends ConnectivityManager.NetworkCallback{
    @Override
    public void onAvailable(Network network) {
        super.onAvailable(network); }
    @Override
    public void onLosing(Network network, int maxMsToLive) {
        super.onLosing(network,maxMsToLive);
    }
    @Override
    public void onLost(Network network) {
        super.onLost(network);
    }
    @Override
    public void onUnavailable() {
        super.onUnavailable();
    }
    @Override
    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
        super.onCapabilitiesChanged(network,networkCapabilities);
    }
    @Override
    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
        super.onLinkPropertiesChanged(network,linkProperties);
    }
    @Override
    public void onBlockedStatusChanged(@NonNull Network network, boolean blocked) {
        super.onBlockedStatusChanged(network,blocked);
    }
}
