package com.libs.module.usb;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.common.base.CustomFontTextView;
import com.common.base.IBaseAdapter;
import com.common.base.ViewHolder;
import com.common.libs.proj.R;
import java.util.List;
/**
 * Created by E on 2018/1/5.
 */
public class UsbFileAdapter extends IBaseAdapter<UsbFile> {

    public UsbFileAdapter(Context context, List<UsbFile> list) {
        super(context, list, R.layout.usb_file_adapter_view);
    }

    @Override
    public void getConvertView(View convertView, List<UsbFile> list, int position) {
        ImageView typeIconImageView = ViewHolder.getView(convertView , R.id.type_icon_imageView);
        CustomFontTextView nameTextView = ViewHolder.getView(convertView , R.id.name_textView);

        UsbFile usbFile = list.get(position);
        String name = usbFile.getName();

        nameTextView.setText(name);

    }
}
