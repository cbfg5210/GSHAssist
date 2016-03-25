package com.hawk.gshassist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.Courses;

import java.util.List;

/**
 * 在此写用途
 * Created by hawk on 2016/3/21.
 */
public class CoursesAdapter extends BaseListAdapter<Courses>{
    public CoursesAdapter(Context ctx, List<Courses> list) {
        super(ctx, list);
    }
    @Override
    public View bindView(int position, View layoutView, ViewGroup viewGroup) {
        if(null==layoutView){
            layoutView=mInflater.inflate(R.layout.item_course,null);
        }
        TextView icoe_kechengmingcheng=ViewHolder.getView(layoutView,R.id.icoe_kechengmingcheng);
        TextView icoe_shangkeshijian=ViewHolder.getView(layoutView,R.id.icoe_shangkeshijian);
        TextView icoe_shangkedidian=ViewHolder.getView(layoutView,R.id.icoe_shangkedidian);
        TextView icoe_renkejiaoshi=ViewHolder.getView(layoutView,R.id.icoe_renkejiaoshi);

        Courses item=mList.get(position);
        icoe_kechengmingcheng.setText(item.getKechengmingcheng());
        icoe_shangkeshijian.setText(item.getShangkeshijian());
        icoe_shangkedidian.setText(item.getShangkedidian());
        icoe_renkejiaoshi.setText(item.getRenkejiaoshi());

        return layoutView;
    }
}
