package com.utils.lib.ss.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
/**
 * Created by E on 2018/2/8.
 */
public interface HttpInterface {
    /**
     * 向服务器发送请求，并返回数据。
     * @param urls 服务器地址,多个地址用于轮询
     * @return 服务器的返回值
     * @throws Exception 请求异常（IOException...）
     */
    String sendRequest(String... urls) throws Exception;
    /**
     * 向服务器发送请求，并返回数据。
     * @param context 上下文环境
     * @param urls 服务器地址,多个地址用于轮询
     * @return 服务器的返回值
     * @throws Exception 请求异常（IOException...）
     */
    String sendRequest(Context context, String... urls) throws Exception;

    /**
     * 向服务器发送请求，并将数据放在Handler中返回。
     * @param context 上下文环境
     * @param urls 服务器地址,多个地址用于轮询
     * @param handler 自定义的Handler
     */
    void sendRequest(Context context, Handler handler, String... urls);

    /**
     * 向服务器发送请求，并将数据放在Handler中返回。
     * @param context 上下文环境
     * @param urls 服务器地址,多个地址用于轮询
     * @param handler 自定义的Handler
     * @param resultWhats 自定义的WHATS,用于接Handler中的数据
     */
    void sendRequest(Context context, Handler handler, int resultWhats, String... urls);

    /**
     * 向服务器发送请求，并将数据放在Message中,通过Handler返回。
     * @param context 上下文环境
     * @param urls 服务器地址,多个地址用于轮询
     * @param handler 自定义的Handler
     * @param msg 自定义的Message
     */
    void sendRequest(Context context, Handler handler, Message msg, String... urls);

}
