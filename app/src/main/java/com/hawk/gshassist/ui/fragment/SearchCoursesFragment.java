package com.hawk.gshassist.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.adapter.CoursesAdapter;
import com.hawk.gshassist.model.Courses;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

/**
 * 由于没有测试账号，课程查询功能暂时不能实现
 */
public class SearchCoursesFragment extends BaseFragment {
    private static final String TAG="SearchCoursesFragment";
    private TextView fscos_tip;
    private ListView fscos_list;
    private CoursesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_course for this fragment
        View layoutView=inflater.inflate(R.layout.fragment_search_courses,null);
        fscos_tip= (TextView) layoutView.findViewById(R.id.fscos_tip);
        fscos_list= (ListView) layoutView.findViewById(R.id.fscos_list);
        adapter=new CoursesAdapter(getContext(),null);
        fscos_list.setAdapter(adapter);

//        getCourses();

        return layoutView;
    }

    private void getCourses(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(GSHNets.NET_EAS_COURSE)
                            .userAgent("Mozilla")
                            .cookies(mListener.getCookies(1))
                            .timeout(3000)
                            .get();
                    String docString=doc.toString();

                } catch (IOException e) {
                    Log.i(TAG, "获取课程信息出现异常");
                }
            }
        }).start();
    }

    private void showCourses(final List<Courses>coursesList){
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.setList(coursesList);
            }
        });
    }

    private void noCourses(final String msg){
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fscos_tip.setText("没有获取到课程信息,原因是:"+msg);
            }
        });
    }

    @Override
    public void refreshFragment() {
        ToastUtil.show("刷新fragment");
    }
}
