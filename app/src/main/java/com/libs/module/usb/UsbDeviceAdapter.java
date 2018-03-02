package com.libs.module.usb;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.view.View;
import com.common.base.CustomFontTextView;
import com.common.base.IBaseAdapter;
import com.common.base.ViewHolder;
import com.common.libs.proj.R;
import java.util.List;
/**
 * Created by Administrator on 2018/1/4.
 */

public class UsbDeviceAdapter extends IBaseAdapter<UsbBean> {

    public UsbDeviceAdapter(Context context, List<UsbBean> list) {
        super(context, list, R.layout.usb_device_adapter_view);
    }

    @Override
    public void getConvertView(View convertView, List<UsbBean> list, int position) {
        CustomFontTextView nameTextView = ViewHolder.getView(convertView , R.id.name_textView);

        UsbBean usbBean = list.get(position);
        UsbDevice usbDevice = usbBean.getUsbDevice();

        nameTextView.setText(usbBean.getName() + "    : " + usbDevice.getDeviceName());

    }
}
