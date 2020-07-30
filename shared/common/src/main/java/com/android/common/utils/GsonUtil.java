package com.android.common.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class GsonUtil {

    public static String ObjectToString(Object o) {
        return new Gson().toJson(o);
    }

    /**
     * 用来解析对象
     */
    public static <T> T StringToObject(String o, Class<T> clazz) {
        return new Gson().fromJson(o, clazz);
    }

    /**
     * 用来解析集合
     */
    public static <T> ArrayList<T> stringToArray(String json, Class<T> clazz) {
        Type type = new TypeToken<ArrayList<JsonObject>>() {
        }.getType();
        ArrayList<JsonObject> jsonObjects = new Gson().fromJson(json, type);
        ArrayList<T> arrayList = new ArrayList<>();
        if (null != jsonObjects){
            for (JsonObject jsonObject : jsonObjects) {
                arrayList.add(new Gson().fromJson(jsonObject, clazz));
            }
        }

        return arrayList;
    }
}
