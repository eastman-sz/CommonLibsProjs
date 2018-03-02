package com.libs.module.noti;

import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;

import com.ble.lib.application.BleApplication;
import com.common.libs.util.ILog;
import java.lang.reflect.Field;
/**
 * Created by E on 2018/1/23.
 */
public class MyNotificationListenService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        ILog.e("---------****onNotificationPosted****------------------");

        Notification notification = sbn.getNotification();
        if (null == notification){
            return;
        }
        String packageName = sbn.getPackageName();
        if (TextUtils.isEmpty(packageName)){
            return;
        }
        //只监听QQ和微信

        //com.tencent.mobileqq  QQ
        //com.tencent.mm  微信
        ILog.e("packageName: " + packageName);

        if (!packageName.equals("com.tencent.mobileqq")
                && !packageName.equals("com.tencent.mm")){
            return;
        }

        // 标题和时间
        String title = "";
        if (notification.tickerText != null) {
            title = notification.tickerText.toString();
        }
        long when = notification.when;
        // 其它的信息存在一个bundle中，此bundle在android4.3及之前是私有的，需要通过反射来获取；android4.3之后可以直接获取
        Bundle bundle = null;
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // android 4.3
            try {
                Field field = Notification.class.getDeclaredField("extras");
                bundle = (Bundle) field.get(notification);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2) {
            // android 4.3之后
            bundle = notification.extras;
        }
        // 内容标题、内容、副内容
        String contentTitle = bundle.getString(Notification.EXTRA_TITLE);
        if (contentTitle == null) {
            contentTitle = "";
        }
        String contentText = bundle.getString(Notification.EXTRA_TEXT);
        if (contentText == null) {
            contentText = "";
        }
        String contentSubtext = bundle.getString(Notification.EXTRA_SUB_TEXT);
        if (contentSubtext == null) {
            contentSubtext = "";
        }
        ILog.e("解析通知栏消息: "+"notify msg: title=" + title + " ,when=" + when
                + " ,contentTitle=" + contentTitle + " ,contentText="
                + contentText + " ,contentSubtext=" + contentSubtext);

        Intent intent = new Intent();
        intent.setAction("newBraceletPushMsgReceived");
        intent.putExtra("title" , title);
        intent.putExtra("type" , packageName.equals("com.tencent.mobileqq") ? 4 : 3);

        BleApplication.getContext().sendBroadcast(intent);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        ILog.e("---------onNotificationRemoved------------------");

    }
}
