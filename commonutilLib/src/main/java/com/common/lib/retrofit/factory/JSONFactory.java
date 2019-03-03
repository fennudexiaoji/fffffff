package com.common.lib.retrofit.factory;

import com.common.lib.retrofit.model.BaseResponse;
import com.common.lib.retrofit.model.StringResponseDeserializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;


public class JSONFactory {
    private static Type type = new TypeToken<BaseResponse<String>>() {
    }.getType();

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(type, new StringResponseDeserializer())
            .create();

    private JSONFactory() {

    }

    public static String toJson(Object o) {
        return gson.toJson(o);
    }

    public static <T> T fromJson(String string, Class<T> tClass) {
        return gson.fromJson(string, tClass);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

}
