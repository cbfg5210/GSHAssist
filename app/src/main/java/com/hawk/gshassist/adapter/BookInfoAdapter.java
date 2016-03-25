package com.hawk.gshassist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.BookInfo;

import java.util.List;

/**
 * 在此写用途
 * Created by hawk on 2016/3/20.
 */
public class BookInfoAdapter extends BaseListAdapter<BookInfo>{
    public BookInfoAdapter(Context ctx, List<BookInfo> list) {
        super(ctx, list);
    }
    @Override
    public View bindView(int position, View layoutView, ViewGroup viewGroup) {
        if(null==layoutView){
            layoutView=mInflater.inflate(R.layout.item_bookinfo,null);
        }
        TextView iboo_timing=ViewHolder.getView(layoutView,R.id.iboo_timing);
        TextView iboo_zerenzhe=ViewHolder.getView(layoutView,R.id.iboo_zerenzhe);
        TextView iboo_chubanshe=ViewHolder.getView(layoutView,R.id.iboo_chubanshe);
        TextView iboo_chubannian=ViewHolder.getView(layoutView,R.id.iboo_chubannian);
        TextView iboo_suoquhao=ViewHolder.getView(layoutView,R.id.iboo_suoquhao);
        TextView iboo_guancang=ViewHolder.getView(layoutView,R.id.iboo_guancang);
        TextView iboo_kejie=ViewHolder.getView(layoutView,R.id.iboo_kejie);
        TextView iboo_xiangguanziyuan=ViewHolder.getView(layoutView,R.id.iboo_xiangguanziyuan);

        BookInfo item=mList.get(position);
        iboo_timing.setText(item.getTiming());
        iboo_zerenzhe.setText(item.getZerenzhe());
        iboo_chubanshe.setText(item.getChubanshe());
        iboo_chubannian.setText(item.getChubannian());
        iboo_suoquhao.setText(item.getSuoquhao());
        iboo_guancang.setText(item.getGuancang());
        iboo_kejie.setText(item.getKejie());
        iboo_xiangguanziyuan.setText(item.getXiangguanziyuan());

        return layoutView;
    }
}
