package com.hawk.gshassist.ui.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.adapter.HisborrowAdapter;
import com.hawk.gshassist.model.HisBorrowInfo;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 *有待优化的地方是没有数据时的提示
 */
public class HistoryBorrowFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "HistoryBorrowFragment";
    private TextView fcbow_tip, fhbow_page;
    private ListView fcbow_list;
    private Button fhbow_lastpage, fhbow_nextpage;
    private HisborrowAdapter adapter;
    private int pageCount;
    private int currentPage = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView = inflater.inflate(R.layout.fragment_history_borrow, null);
        fcbow_tip = (TextView) layoutView.findViewById(R.id.fcbow_tip);
        fhbow_page = (TextView) layoutView.findViewById(R.id.fhbow_page);
        fcbow_list = (ListView) layoutView.findViewById(R.id.fcbow_list);
        fhbow_lastpage = (Button) layoutView.findViewById(R.id.fhbow_lastpage);
        fhbow_nextpage = (Button) layoutView.findViewById(R.id.fhbow_nextpage);
        adapter = new HisborrowAdapter(getContext(), null);
        fcbow_list.setAdapter(adapter);

        fhbow_lastpage.setOnClickListener(this);
        fhbow_nextpage.setOnClickListener(this);

        getHisborrowBooks(currentPage, true);

        return layoutView;
    }

    /**
     * 获取第某页数据
     *
     * @param page
     * @param withPageCount 是否返回页数
     */
    private void getHisborrowBooks(final int page, final boolean withPageCount) {
        mListener.showProgress(null, "正在获取历史借阅数据,请稍候...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bundle data = new Bundle();
                String url = GSHNets.NET_LIB_HISBORROWED + "?page=" + page;
                try {
                    Connection.Response rps = Jsoup.connect(url)
                            .cookies(mListener.getCookies(1))
                            .method(Connection.Method.GET)
                            .followRedirects(true)
                            .execute();

                    Document doc = rps.parse();
                    if (TextUtils.isEmpty(doc.toString())) return;
                    data = new Bundle();

                    if (withPageCount) {
                        Element pageCount = doc.select("#ctl00_cpRight_Pagination1_gplbl2").first();
                        if (null == pageCount||"0".equals(pageCount.text().trim())){
                            mListener.dismissProgress();
                            ToastUtil.show("没有借阅数据");
                            return;
                        }
                        int pagecount = Integer.parseInt(pageCount.text().trim());  //!!!!当没有历史借阅信息时,pageCount.text()的值应该是什么?
                        data.putInt("pagecount", pagecount);
                    }

                    ArrayList<HisBorrowInfo> hisborrowList = new ArrayList<HisBorrowInfo>();
                    Element subinfolines = doc.select("tbody").first();
                    Elements subtrs = subinfolines.select("tr");
                    int len = subtrs.size();
                    for (int j = 0; j < len; j++) {
                        HisBorrowInfo item = new HisBorrowInfo();
                        Element element = subtrs.get(j);
                        Elements tds = element.select("td"); //td

                        item.setJieqi(tds.get(0).text());
                        item.setHuanqi(tds.get(1).text());
                        item.setTiming(tds.get(2).text());
                        item.setTushuleixing(tds.get(3).text());
                        item.setDengluhao(tds.get(4).text());

                        hisborrowList.add(item);
                    }
                    data.putParcelableArrayList("hisborrowList", hisborrowList);
                    showHistoryBooks(data);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "解析历史借阅数据出现异常:" + e.getMessage());
                    mListener.dismissProgress();
                    ToastUtil.show("获取数据失败");
                }
            }
        }).start();
    }

    private void showHistoryBooks(final Bundle data) {
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (data.containsKey("pagecount")) {
                    pageCount = data.getInt("pagecount");
                }
                fhbow_page.setText(currentPage + "/" + pageCount);
                ArrayList<HisBorrowInfo> hisborrowList = data.getParcelableArrayList("hisborrowList");
                adapter.setList(hisborrowList);
                mListener.dismissProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.fhbow_lastpage) {
            if (currentPage <= 1) {
                ToastUtil.show("没有上一页了");
                return;
            }
            ToastUtil.show("开始获取上一页数据");
            currentPage--;
            getHisborrowBooks(currentPage, false);
        } else if (vid == R.id.fhbow_nextpage) {
            if (currentPage >= pageCount) {
                ToastUtil.show("没有下一页了");
                return;
            }
            ToastUtil.show("开始获取下一页数据");
            currentPage++;
            getHisborrowBooks(currentPage, false);
        }
    }

    @Override
    public void refreshFragment() {
        currentPage=1;
        getHisborrowBooks(currentPage, true);
    }
}
