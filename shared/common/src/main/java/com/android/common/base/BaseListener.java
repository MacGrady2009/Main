package com.android.common.base;

/**
 * @author cds create on 2018/6/26.
 */
public interface BaseListener<T,V> extends BaseInterface{
    void onInit(T t, V v);
}
