package com.cbase.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

/**
 * @author : zhouyx
 * @date : 2016/5/11
 * @description : Json工具
 */
public class JsonUtils {

    private static Gson sGson;

    static {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(JsonObject.class, new JsonDeserializer<Object>() {
            @Override
            public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return json.getAsJsonObject();
            }
        });
        sGson = builder.disableHtmlEscaping().create();
    }

    private static boolean isEmpty(Object obj) {
        if (obj == null || "".equals(obj) || "null".equals(obj)) {
            return true;
        }
        return false;
    }

    /**
     * 对象转换成json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return sGson.toJson(object);
    }

    /**
     * json字符串转换成对象
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Class<T> cls) {
        if (isEmpty(json) || cls == null) {
            return null;
        }
        try {
            return sGson.fromJson(json, cls);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json字符串转换成对象（可转换为List）
     * 例 : TypeToken<T> token = new TypeToken<ArrayList<T>>(){}
     *
     * @param json
     * @param token
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        if (isEmpty(json) || token == null) {
            return null;
        }
        try {
            return sGson.fromJson(json, token.getType());
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从json中返回键的对象
     *
     * @param json
     * @param name
     * @return
     */
    public static Object findObject(String json, String name) {
        if (isEmpty(json) || isEmpty(name)) {
            return null;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (!jsonObject.has(name)) {
                return null;
            }
            return jsonObject.get(name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
