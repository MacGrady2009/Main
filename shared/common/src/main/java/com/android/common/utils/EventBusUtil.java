package com.android.common.utils;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {

    /**
     * 注册 EventBus
     */
    public static void register(Object subscriber) {
        try {
            if (!EventBus.getDefault().isRegistered(subscriber)) {
                EventBus.getDefault().register(subscriber);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 取消注册
     */
    public static void unregister(Object subscriber) {
        if (EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().unregister(subscriber);
        }
    }
}
