package com.common.libs.proj;

import android.content.Context;
import android.view.View;
import com.common.base.CustomFontTextView;
import com.common.base.IBaseAdapter;
import com.common.base.ViewHolder;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by E on 2017/10/20.
 */
public class TextAdapter extends IBaseAdapter<String> {

    public TextAdapter(Context context ,ArrayList<String> list) {
        super(context , list , R.layout.text_adapter_view);
    }

    @Override
    public void getConvertView(View convertView, List<String> list, int position) {
        CustomFontTextView customFontTextView = ViewHolder.getView(convertView , R.id.text);

        customFontTextView.setText(list.get(position));
    }
}
