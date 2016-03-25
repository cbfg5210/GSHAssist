package com.hawk.gshassist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.CurBorrowInfo;

import java.util.List;

/**
 * 在此写用途
 * Created by hawk on 2016/3/20.
 */
public class CurborrowAdapter extends BaseListAdapter<CurBorrowInfo>{
    public CurborrowAdapter(Context ctx, List<CurBorrowInfo> list) {
        super(ctx, list);
    }
    @Override
    public View bindView(int position, View layoutView, ViewGroup viewGroup) {
        if(null==layoutView){
            layoutView=mInflater.inflate(R.layout.item_current_borrow,null);
        }
        TextView icbow_xujie=ViewHolder.getView(layoutView,R.id.icbow_xujie);
        TextView icbow_zuichiyinghuanqi=ViewHolder.getView(layoutView,R.id.icbow_zuichiyinghuanqi);
        TextView icbow_timing=ViewHolder.getView(layoutView,R.id.icbow_timing);
        TextView icbow_juanqi=ViewHolder.getView(layoutView,R.id.icbow_juanqi);
        TextView icbow_tushuleixing=ViewHolder.getView(layoutView,R.id.icbow_tushuleixing);
        TextView icbow_dengluhao=ViewHolder.getView(layoutView,R.id.icbow_dengluhao);
        TextView icbow_jieqi=ViewHolder.getView(layoutView,R.id.icbow_jieqi);

        CurBorrowInfo item=mList.get(position);
        icbow_xujie.setText(item.getXujie());
        icbow_zuichiyinghuanqi.setText(item.getZuichiyinghuanqi());
        icbow_timing.setText(item.getTiming());
        icbow_juanqi.setText(item.getJuanqi());
        icbow_tushuleixing.setText(item.getTushuleixing());
        icbow_dengluhao.setText(item.getDengluhao());
        icbow_jieqi.setText(item.getJieqi());

        return layoutView;
    }
}
