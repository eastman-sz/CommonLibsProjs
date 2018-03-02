package com.utils.lib.ss.http;

import android.text.TextUtils;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;
/**
 * Created by E on 2018/2/8.
 */
public abstract class BaseHttpImpl implements HttpInterface{

    private static final String CHARSET = "UTF-8";

    private String map2Url(Map<String , Object> params) throws Exception{
        params = specialHandleParams(params);
        if (null == params || params.isEmpty()){
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, Object>> entrys = params.entrySet();
        for (Map.Entry<String, Object> entry : entrys){
            String key = entry.getKey();
            Object value = entry.getValue();
            if (TextUtils.isEmpty(key) || null == value){
                continue;
            }
            builder.append(key).append("=")
                   .append(value instanceof String ? URLEncoder.encode(String.valueOf(value), CHARSET) : value)
                   .append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    public abstract Map<String, Object> specialHandleParams(Map<String, Object> params);


}
