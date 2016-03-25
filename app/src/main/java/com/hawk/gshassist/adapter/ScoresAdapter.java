package com.hawk.gshassist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.Scores;

import java.util.List;

/**
 * 在此写用途
 * Created by hawk on 2016/3/18.
 */
public class ScoresAdapter extends BaseListAdapter<Scores>{
    public ScoresAdapter(Context ctx, List<Scores> list) {
        super(ctx, list);
    }
    @Override
    public View bindView(int position, View layoutView, ViewGroup viewGroup) {
        if(null==layoutView){
            layoutView=mInflater.inflate(R.layout.item_scores,null);
        }
        TextView iscs_xueqi=ViewHolder.getView(layoutView,R.id.iscs_xueqi);
        TextView iscs_kemu=ViewHolder.getView(layoutView,R.id.iscs_kemu);
        TextView iscs_xuefen=ViewHolder.getView(layoutView,R.id.iscs_xuefen);
        TextView iscs_qimozongping=ViewHolder.getView(layoutView,R.id.iscs_qimozongping);
        TextView iscs_bukaochengjiyi=ViewHolder.getView(layoutView,R.id.iscs_bukaochengjiyi);
        TextView iscs_bukaochengjier=ViewHolder.getView(layoutView,R.id.iscs_bukaochengjier);
        TextView iscs_bukaochengjisan=ViewHolder.getView(layoutView,R.id.iscs_bukaochengjisan);

        Scores item=mList.get(position);
        iscs_xueqi.setText("学期："+item.getXueqi());
        iscs_kemu.setText("科目："+item.getKemu());
        iscs_xuefen.setText("学分："+item.getXuefen());
        iscs_qimozongping.setText("期末总评："+item.getQimozongping());
        iscs_bukaochengjiyi.setText("补考成绩1："+item.getBukaochengjiyi());
        iscs_bukaochengjier.setText("补考成绩2："+item.getBukaochengjier());
        iscs_bukaochengjisan.setText("补考成绩3："+item.getBukaochengjisan());

        return layoutView;
    }
}
