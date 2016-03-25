package com.hawk.gshassist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.adapter.BaseListAdapter;
import com.hawk.gshassist.adapter.ViewHolder;
import com.hawk.gshassist.model.HisBorrowInfo;

import java.util.List;

/**
 * 在此写用途
 * Created by hawk on 2016/3/20.
 */
public class HisborrowAdapter extends BaseListAdapter<HisBorrowInfo>{
    public HisborrowAdapter(Context ctx, List<HisBorrowInfo> list) {
        super(ctx, list);
    }
    @Override
    public View bindView(int position, View layoutView, ViewGroup viewGroup) {
        if(null==layoutView){
            layoutView=mInflater.inflate(R.layout.item_hisborrow,null);
        }
        TextView ihiw_jieqi= ViewHolder.getView(layoutView,R.id.ihiw_jieqi);
        TextView ihiw_huanqi= ViewHolder.getView(layoutView,R.id.ihiw_huanqi);
        TextView ihiw_timing= ViewHolder.getView(layoutView,R.id.ihiw_timing);
        TextView ihiw_tushuleixing= ViewHolder.getView(layoutView,R.id.ihiw_tushuleixing);
        TextView ihiw_dengluhao= ViewHolder.getView(layoutView,R.id.ihiw_dengluhao);

        HisBorrowInfo item=mList.get(position);
        ihiw_jieqi.setText(item.getJieqi());
        ihiw_huanqi.setText(item.getHuanqi());
        ihiw_timing.setText(item.getTiming());
        ihiw_tushuleixing.setText(item.getTushuleixing());
        ihiw_dengluhao.setText(item.getDengluhao());

        return layoutView;
    }
}
