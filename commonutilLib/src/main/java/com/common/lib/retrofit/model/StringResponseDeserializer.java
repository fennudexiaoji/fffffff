package com.common.lib.retrofit.model;


import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.List;

public class StringResponseDeserializer implements JsonDeserializer<BaseResponse<String>> {
    private Gson gson = new Gson();

    @Override
    public BaseResponse<String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        BaseResponse<String> baseResponse = new BaseResponse<>();
        if (json.isJsonObject()) {
            JsonObject asJsonObject = json.getAsJsonObject();
            JsonElement data = asJsonObject.get("data");
//            JsonElement status = asJsonObject.get("status");
            JsonElement code = asJsonObject.get("code");
            JsonElement msg = asJsonObject.get("msg");

            baseResponse.setError(code.getAsInt());
//            baseResponse.setStatus(status.getAsBoolean());
            /*List<String> listMsg=baseResponse.getMsg();
            String msg=listMsg.size()>0?listMsg.get(0).toString():"";*/
            //baseResponse.setMsg(msg.getAsString() == null ? "" : msg.getAsString());
            if (data != null) {
                if (data.isJsonArray() || data.isJsonObject()) {
                    String s = gson.toJson(data);
                    baseResponse.setData(s);
                    return baseResponse;
                } else if (data.isJsonNull()) {
                    //data为null,不做处理
                } else {
                    baseResponse.setData(data.getAsString());
                }
            }
        }
        return baseResponse;
    }
}