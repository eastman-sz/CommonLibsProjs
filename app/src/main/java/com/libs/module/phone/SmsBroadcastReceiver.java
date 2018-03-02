package com.libs.module.phone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import com.ble.lib.application.BleApplication;
import com.common.libs.util.ILog;

/**
 * Created by E on 2018/1/24.
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action)){
            return;
        }
        if (!SMS_RECEIVED.equals(action)){
            return;
        }

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            final SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
            }
            if (messages.length > 0) {
                String content = messages[0].getMessageBody();
                String sender = messages[0].getOriginatingAddress();
                long msgDate = messages[0].getTimestampMillis();

                ILog.e("message from: " + sender + ", message body: " + content
                        + ", message date: " + msgDate);

                //发送信息给手环
                String title = sender + content;

                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction("newBraceletPushMsgReceived");
                broadcastIntent.putExtra("title" , title);
                broadcastIntent.putExtra("type" , 2);

                BleApplication.getContext().sendBroadcast(broadcastIntent);

            }
        }

    }
}
