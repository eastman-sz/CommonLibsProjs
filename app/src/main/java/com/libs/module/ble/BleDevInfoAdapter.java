package com.libs.module.ble;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.ble.lib.dev.BleDevInfo;
import com.ble.lib.f.BleState;
import com.common.base.CustomFontTextView;
import com.common.base.IBaseAdapter;
import com.common.base.ViewHolder;
import com.common.libs.proj.R;
import java.util.List;
/**
 * Created by E on 2017/12/7.
 */
public class BleDevInfoAdapter extends IBaseAdapter<BleDevInfo>{

    private int color1 = 0;
    private int color2 = 0;
    private int color3 = 0;

    public BleDevInfoAdapter(Context context, List<BleDevInfo> list) {
        super(context, list, R.layout.ble_devinfo_adapter_view);

        color1 = context.getResources().getColor(R.color.c12);
        color2 = context.getResources().getColor(R.color.c22);
        color3 = context.getResources().getColor(R.color.c27);

    }

    @Override
    public void getConvertView(View convertView, List<BleDevInfo> list, int position) {
        LinearLayout bgLayout = ViewHolder.getView(convertView , R.id.bgLayout);
        CustomFontTextView nameTextView = ViewHolder.getView(convertView , R.id.name_textView);
        CustomFontTextView addressTextView = ViewHolder.getView(convertView , R.id.address_textView);

        BleDevInfo devInfo = list.get(position);
        String name = devInfo.getName();
        String address = devInfo.getAddress();
        int state = devInfo.getState();
        boolean foucsed = devInfo.isFocused();

        nameTextView.setText(name);
        addressTextView.setText(address);

        nameTextView.setTextColor(state == BleState.STATE_CONNECTED.getState() ? color3
                : state == BleState.STATE_CONNECTING.getState() ? color2
                : color1);

        if (foucsed){
            bgLayout.setBackgroundColor(context.getResources().getColor(R.color.c1));
        }else {
            bgLayout.setBackgroundColor(context.getResources().getColor(R.color.c10));
        }

    }
}
