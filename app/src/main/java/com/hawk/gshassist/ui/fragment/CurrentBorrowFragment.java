package com.hawk.gshassist.ui.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.adapter.CurborrowAdapter;
import com.hawk.gshassist.model.CurBorrowInfo;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 *有待优化的地方是没有数据时的提示
 */
public class CurrentBorrowFragment extends BaseFragment {
    private static final String TAG="CurrentBorrowFragment";
    private TextView fcbow_tip;
    private ListView fcbow_list;
    private CurborrowAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_current_borrow, null);
        fcbow_tip= (TextView) layoutView.findViewById(R.id.fcbow_tip);
        fcbow_list= (ListView) layoutView.findViewById(R.id.fcbow_list);
        adapter=new CurborrowAdapter(getContext(),null);
        fcbow_list.setAdapter(adapter);

        getCurborrowBooks();

        return layoutView;
    }

    //因为当前借阅书籍数量有限制，所以可以一次返回全部
    private void getCurborrowBooks() {
        mListener.showProgress(null,"正在获取当前借阅数据,请稍候...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(GSHNets.NET_LIB_CURBORROWING)
                            .cookies(mListener.getCookies(1)).get();

                    ArrayList<CurBorrowInfo> curborrowList=new ArrayList<CurBorrowInfo>();

                    Element infolines = doc.select("tbody").first();
                    Elements trs = infolines.select("tr");
                    for (int i = 0; i < trs.size(); i++) {
                        Element element = trs.get(i);
                        Elements tds = element.select("td"); //td

                        CurBorrowInfo item=new CurBorrowInfo();

                        item.setXujie(tds.get(0).text());
                        item.setZuichiyinghuanqi(tds.get(1).text());
                        item.setTiming(tds.get(2).text());
                        item.setJuanqi(tds.get(3).text());
                        item.setTushuleixing(tds.get(4).text());
                        item.setDengluhao(tds.get(5).text());
                        item.setJieqi(tds.get(6).text());

                        curborrowList.add(item);
                    }
                    showCurborrowBooks(curborrowList);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG,"获取或解析当前借阅数据失败:"+e.getMessage());
                    mListener.dismissProgress();
                    ToastUtil.show("获取数据失败");
                }
            }
        }).start();
    }

    private void showCurborrowBooks(final ArrayList<CurBorrowInfo> curborrowList){
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setList(curborrowList);
                mListener.dismissProgress();
            }
        });
    }

    @Override
    public void refreshFragment() {
        getCurborrowBooks();
    }
}
