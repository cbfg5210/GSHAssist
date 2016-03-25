package com.hawk.gshassist.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.adapter.ScoresAdapter;
import com.hawk.gshassist.model.Scores;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

public class SearchScoresFragment extends BaseFragment {
    private static final String TAG = "SearchScoresFragment";
    private ListView fsscs_list;
    private ScoresAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_course for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_search_scores, null);
        fsscs_list = (ListView) layoutView.findViewById(R.id.fsscs_list);
        adapter = new ScoresAdapter(getContext(), null);
        fsscs_list.setAdapter(adapter);

        showScores();

        return layoutView;
    }

    //获取成绩数据
    private void showScores() {
        mListener.showProgress(null, "正在获取成绩数据,请稍候...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect(GSHNets.NET_EAS_SCORE)
                            .userAgent("Mozilla")
                            .cookies(mListener.getCookies(0))
                            .timeout(3000)
                            .get();

                    String docStr = doc.toString();
                    int indx = docStr.indexOf("RecordCount");
                    indx = docStr.indexOf("Values", indx);
                    indx = docStr.indexOf("[[", indx);
                    int ed = docStr.indexOf("]]") + 2;
                    docStr = docStr.substring(indx, ed);//将完整的成绩内容截取出来再做解析
                    //完整数据格式:[[xxx,xxx],[yyy,yyy]]

                    JSONArray jarr = new JSONArray(docStr);
                    int len = jarr.length();
                    final List<Scores> scoresList = new ArrayList<Scores>(len);
                    for (int i = 0; i < len; i++) {
                        JSONArray subJarr = jarr.optJSONArray(i);
                        Scores item = new Scores();
                        item.setXueqi(subJarr.optString(0));
                        item.setKemu(subJarr.optString(1));
                        item.setXuefen(subJarr.optString(2));
                        item.setQimozongping(subJarr.optString(3));
                        item.setBukaochengjiyi(subJarr.optString(4));
                        item.setBukaochengjier(subJarr.optString(5));
                        item.setBukaochengjisan(subJarr.optString(6));
                        scoresList.add(item);
                    }

                    getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setList(scoresList);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i(TAG, "json获取成绩信息出现异常:" + e.getMessage());
                    ToastUtil.show("无法获取到数据");
                }
                mListener.dismissProgress();
            }
        }).start();
    }

    @Override
    public void refreshFragment() {
        showScores();
    }
}
