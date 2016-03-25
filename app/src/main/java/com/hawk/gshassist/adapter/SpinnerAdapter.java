package com.hawk.gshassist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.SpinnerInfo;

import java.util.List;

/**
 * 在此写用途
 * Created by hawk on 2016/3/18.
 */
public class SpinnerAdapter extends BaseListAdapter<SpinnerInfo>{
    public SpinnerAdapter(Context ctx, List<SpinnerInfo> list) {
        super(ctx, list);
    }
    @Override
    public View bindView(int position, View layoutView, ViewGroup viewGroup) {
        if(null==layoutView){
            layoutView=mInflater.inflate(R.layout.item_xueyuan,null);
        }
        TextView ixun_name=ViewHolder.getView(layoutView,R.id.ixun_name);
        SpinnerInfo item=mList.get(position);
        ixun_name.setText(item.getName());

        return layoutView;
    }
}
