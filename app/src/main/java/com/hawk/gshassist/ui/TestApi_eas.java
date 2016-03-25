package com.hawk.gshassist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hawk.gshassist.R;
import com.hawk.gshassist.adapter.SpinnerAdapter;
import com.hawk.gshassist.model.EASProfile;
import com.hawk.gshassist.model.Scores;
import com.hawk.gshassist.model.SpinnerInfo;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestApi_eas extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestApi";
    private Map<String, String> params;
    private Map<String, String> cookies;
    private Button aaa, bbb, ccc, ddd, selectyuanxi;
    private EditText tip;
    private String logInfo;
    private Spinner xueyuan, banji;
    private SpinnerAdapter xueyuanAdapter, banjiAdapter;
    private Bundle bundle;
    private boolean isFirst = true;//是否是第一次获取课程数据，如果是的话，要返回SelectedValue
    private boolean isFirstSelected = true;//spinner是否是第一次改变选中

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_testapi_eas);

        aaa = (Button) findViewById(R.id.aaa);//登录
        bbb = (Button) findViewById(R.id.bbb);//成绩
        ccc = (Button) findViewById(R.id.ccc);//课程
        ddd = (Button) findViewById(R.id.ddd);//个人信息
        tip = (EditText) findViewById(R.id.tip);
        xueyuan = (Spinner) findViewById(R.id.xueyuan);
        banji = (Spinner) findViewById(R.id.banji);
        selectyuanxi = (Button) findViewById(R.id.selectyuanxi);

        xueyuanAdapter = new SpinnerAdapter(this, null);
        xueyuan.setAdapter(xueyuanAdapter);
        banjiAdapter = new SpinnerAdapter(this, null);
        banji.setAdapter(banjiAdapter);

        aaa.setOnClickListener(this);
        bbb.setOnClickListener(this);
        ccc.setOnClickListener(this);
        ddd.setOnClickListener(this);
        selectyuanxi.setOnClickListener(this);

        xueyuan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ToastUtil.show("第一次设置选中");
                //第一次设置选中项的话忽略
                if (isFirstSelected) {
                    return;
                }
                final SpinnerInfo info = (SpinnerInfo) parent.getItemAtPosition(position);
                //更新班级下拉列表
                ToastUtil.show("开始更新班级下拉列表");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        updateBanjiSpinnerItems(info.getFlagStr());
                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        banji.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //第一次设置选中项的话忽略
                if (isFirstSelected) {
                    isFirstSelected = false;
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    }
                }).start();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //    cookie有过期时间，要设置定时重新登录
//    可以使用android里的定时任务
    private void login() {
        params = new HashMap<>();
        params.put("Window1$SimpleForm1$rdl_shenFen", "学生");
        params.put("Window1$SimpleForm1$tbx_XueHao", "12124640117");
        params.put("Window1$SimpleForm1$tbx_pwd", "71104642121");
        params.put("__EVENTTARGET", "Window1$Toolbar1$btn_login");
        params.put(
                "__VIEWSTATE",
                "/wEPDwULLTE5MDM2NzU3OTVkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYGBQdXaW5kb3cxBRNXaW5kb3cxJFNp\n" +
                        "bXBsZUZvcm0xBR5XaW5kb3cxJFNpbXBsZUZvcm0xJHRieF9YdWVIYW8FG1dpbmRvdzEkU2ltcGxlRm9ybTEkdGJ4X3B3ZAUfV2luZG93MSRTaW1wbGVGb3JtMSRyZGxfc2hlbkZlbgUaV2luZG93MSRUb29sYmFyMSRidG5fbG9naW48JqvBYCN12yX7qJv0\n" +
                        "+Skqb/+OMwW2rhBjeJStRa2gMg==");

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response res = Jsoup.connect("http://218.15.22.136:3008/login.aspx")
                            .data(params).timeout(30000).execute();
                    cookies = res.cookies();
                    Log.i(TAG, "cookie=" + cookies);

                    logInfo = cookies.toString();
                    runOnUiThread(tipRunnable);
                } catch (IOException ioe) {
                    Log.i(TAG, "exception");
                }
            }
        }).start();
    }

    //    获取课程表
    private void getCourses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bundle = getSpinnerInfos(0, isFirst);
                if (bundle == null || bundle.isEmpty()) return;
                runOnUiThread(xueyuanRunnable);

                bundle = getSpinnerInfos(1, isFirst);
                if (bundle == null || bundle.isEmpty()) return;
                runOnUiThread(banjiRunnable);

                if (isFirst) isFirst = false;//之后不用再返回SelectedValue数据了
            }
        }).start();
    }

    private void newGetCourses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Grid1$Toolbar1$ddl_BanJi=104800
//                    Grid1$Toolbar1$ddl_XueYuan=12
//                    Grid1_Collapsed=false
//                    Grid1_HiddenColumnIndexArray=
//                            Grid1_SelectedRowIndexArray=
//                                    Grid2_Collapsed=false
//                    Grid2_HiddenColumnIndexArray=
//                            Grid2_SelectedRowIndexArray=
//                                    X_AJAX=true
//                    X_CHANGED=false
//                    X_STATE=eyJHcmlkMV9Ub29sYmFyMV9kZGxfWHVlWXVhbiI6eyJEYXRhVGV4dEZpZWxkIjoiamdtYyIsIkRhdGFWYWx1ZUZpZWxkIjoiamdkbSIsIlhfSXRlbXMiOltbIjAyICAiLCIwNOacuueUteW3peeoi+WtpumZoiAgICAgICAgICAgIiwxXSxbIjAzICAiLCIwNeiuoeeul+acuuS4jueUteWtkOS/oeaBr+WtpumZoiAgICIsMV0sWyIwNCAgIiwiMDblu7rnrZHlt6XnqIvlrabpmaIgICAgICAgICAgICIsMV0sWyIwNSAgIiwiMTDnu4/mtY7nrqHnkIblrabpmaIgICAgICAgICAgICIsMV0sWyIwNiAgIiwiMDfmlofms5XlrabpmaIgICAgICAgICAgICAgICAiLDFdLFsiMDggICIsIjEx5L2T6IKy5a2m57O7ICAgICAgICAgICAgICAgIiwxXSxbIjEwICAiLCIxMuiJuuacr+ezuyAgICAgICAgICAgICAgICAgIiwxXSxbIjEyICAiLCIwOOeQhuWtpumZoiAgICAgICAgICAgICAgICAgIiwxXSxbIjEzICAiLCIwOeWkluWbveivreWtpumZoiAgICAgICAgICAgICAiLDFdLFsiMTQgICIsIjAz546v5aKD5LiO55Sf54mp5bel56iL5a2m6ZmiICAgICAiLDFdLFsiMTUgICIsIjAx55+z5rK55bel56iL5a2m6ZmiICAgICAgICAgICAiLDFdLFsiMTYgICIsIjAy5YyW5a2m5bel56iL5a2m6ZmiICAgICAgICAgICAiLDFdXSwiU2VsZWN0ZWRWYWx1ZSI6IjEyICAifSwiR3JpZDFfVG9vbGJhcjFfZGRsX0JhbkppIjp7IkRhdGFUZXh0RmllbGQiOiJiam1jIiwiRGF0YVZhbHVlRmllbGQiOiJiamRtIiwiWF9JdGVtcyI6W1siMzkwNyIsIuWcsOeQhijluIgpMTEtMSAgICAgICAgICAgICAgICAgICAgICAgICAgICAiLDFdLFsiMTA0MzkwIiwi5Zyw55CGKOW4iCkxMS0zIiwxXSxbIjUxMDIiLCLlnLDnkIYo5biIKTEyLTEgICAgICAiLDFdLFsiMTA0NjczIiwi5Zyw55CGKOW4iCkxMi0yIiwxXSxbIjEwNDI1NCIsIuWcsOeQhijluIgpMTMtMSIsMV0sWyIxMDQ4MzMiLCLlnLDnkIYo5biIKTEzLTIiLDFdLFsiMTA0NjI1Iiwi5Zyw55CGKOW4iCkxNC0xIiwxXSxbIjEwNDc5OCIsIuWcsOeQhijluIgpMTUtMSIsMV0sWyIzOTY2Iiwi5Zyw55CGMTEtMiIsMV0sWyIxMDQ3OTkiLCLlnLDnkIYxNS0yIiwxXSxbIjEwNDgzNCIsIuWcsOeQhjE1LTMiLDFdLFsiMzk2NCIsIuaVmeiCsuaKgOacrzExLTEgICAgICAgICAgICAgICAgICAgICAgICAgICAgIiwxXSxbIjUxMDUiLCLmlZnogrLmioDmnK8xMi0xICAgICAgICAiLDFdLFsiNTEwNiIsIuaVmeiCsuaKgOacrzEyLTIgICAgICAgICIsMV0sWyIxMDQzMTciLCLmlZnogrLmioDmnK8xMy0xIiwxXSxbIjEwNDMxOCIsIuaVmeiCsuaKgOacrzEzLTIiLDFdLFsiMTA0NjI4Iiwi5pWZ6IKy5oqA5pyvMTQtMSIsMV0sWyIxMDQ2MjkiLCLmlZnogrLmioDmnK8xNC0yIiwxXSxbIjEwNDgyMSIsIuaVmeiCsuaKgOacrzE1LTEiLDFdLFsiMTA0ODIyIiwi5pWZ6IKy5oqA5pyvMTUtMiIsMV0sWyIzOTExIiwi5pWw5a2mKOW4iCkxMS0xICAgICAgICAgICAgICAgICAgICAgICAgICAgICIsMV0sWyI1MDgyIiwi5pWw5a2mKOW4iCkxMi0xICAgICAgIiwxXSxbIjEwNDM0MyIsIuaVsOWtpijluIgpMTMtMSIsMV0sWyIxMDQ4MjYiLCLmlbDlraYo5biIKTEzLTMiLDFdLFsiMTA0NjIxIiwi5pWw5a2mKOW4iCkxNC0xIiwxXSxbIjEwNDc5NCIsIuaVsOWtpijluIgpMTUtMSIsMV0sWyIzOTc5Iiwi5pWw5a2mMTEtMiIsMV0sWyI1MDgzIiwi5pWw5a2mMTItMiAgICAgICAgICAgICIsMV0sWyIxMDQ2NTkiLCLmlbDlraYxMi0zIiwxXSxbIjEwNDM0NCIsIuaVsOWtpjEzLTIiLDFdLFsiMTA0ODI3Iiwi5pWw5a2mMTMtNCIsMV0sWyIxMDQ2MjIiLCLmlbDlraYxNC0yIiwxXSxbIjEwNDc5NSIsIuaVsOWtpjE1LTIiLDFdLFsiMzk2NSIsIueJqeeQhijluIgpMTEtMSAgICAgICAgICAgICAgICAgICAgICAgICAgICAiLDFdLFsiNTEwMyIsIueJqeeQhijluIgpMTItMSAgICAgICIsMV0sWyIxMDQzNTkiLCLniannkIYo5biIKTEzLTEiLDFdLFsiMTA0NjI2Iiwi54mp55CGKOW4iCkxNC0xIiwxXSxbIjEwNDgwMCIsIueJqeeQhijluIgpMTUtMSIsMV0sWyIzOTgyIiwi54mp55CGMTEtMiIsMV0sWyI1MTA0Iiwi54mp55CGMTItMiAgICAgICAgICAgICIsMV0sWyIxMDQzNTgiLCLniannkIYxMy0yIiwxXSxbIjEwNDYyNyIsIueJqeeQhjE0LTIiLDFdLFsiMTA0ODAxIiwi54mp55CGMTUtMiIsMV0sWyIzOTgwIiwi5L+h5oGvMTEtMSAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIiwxXSxbIjM5ODEiLCLkv6Hmga8xMS0yICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAiLDFdLFsiNTEwMCIsIuS/oeaBrzEyLTEgICAgICAgICAgICAiLDFdLFsiNTEwMSIsIuS/oeaBrzEyLTIgICAgICAgICAgICAiLDFdLFsiMTA0MzYwIiwi5L+h5oGvMTMtMSIsMV0sWyIxMDQzNjEiLCLkv6Hmga8xMy0yIiwxXSxbIjEwNDYyMyIsIuS/oeaBrzE0LTEiLDFdLFsiMTA0NjI0Iiwi5L+h5oGvMTQtMiIsMV0sWyIxMDQ3OTYiLCLkv6Hmga8xNS0xIiwxXSxbIjEwNDc5NyIsIuS/oeaBrzE1LTIiLDFdLFsiMzA2NSIsIuWcsOeQhijluIgpMTAtMSIsMV0sWyIzMDY2Iiwi5Zyw55CGKOW4iCkxMC0yIiwxXSxbIjMwNjkiLCLmlZnogrLmioDmnK8xMC0xIiwxXSxbIjMwNzAiLCLmlZnogrLmioDmnK8xMC0yIiwxXSxbIjMwNTkiLCLmlbDlraYo5biIKTEwLTEiLDFdLFsiNjAwOCIsIuaVsOWtpijluIgpMTAtMyAgICAgICIsMV0sWyIzMDYwIiwi5pWw5a2mMTAtMiIsMV0sWyIzMDY3Iiwi54mp55CGKOW4iCkxMC0xIiwxXSxbIjMwNjgiLCLniannkIYxMC0yIiwxXSxbIjMwNjEiLCLkv6Hmga8xMC0xIiwxXSxbIjMwNjIiLCLkv6Hmga8xMC0yIiwxXSxbIjMwNjMiLCLkv6Hmga8xMC0zIiwxXSxbIjMwNjQiLCLkv6Hmga8xMC00IiwxXV0sIlNlbGVjdGVkVmFsdWUiOiI1MTAwIn0sIkdyaWQxIjp7IlRpdGxlIjoi5pWZ5a2m5o6I6K++5L+h5oGv6KGoIDxzcGFuIHN0eWxlPSdjb2xvcjpyZWQ7Jz7vvIjmn6Xor6Lnj63nuqfvvJrkv6Hmga8xMi0x77yJPC9zcGFuPiIsIlhfUm93cyI6eyJWYWx1ZXMiOltdLCJEYXRhS2V5cyI6W10sIlN0YXRlcyI6W119fSwiR3JpZDIiOnsiVGl0bGUiOiLmlZnlrablkajov5vnqIvooaggPHNwYW4gc3R5bGU9J2NvbG9yOnJlZDsnPijogIPor5Xnp5Hnm67vvJrml6DogIPor5Xnp5Hnm67vvIk8L3NwYW4+IiwiUmVjb3JkQ291bnQiOjEsIlhfUm93cyI6eyJWYWx1ZXMiOltbIuKWsyAgIiwi4pazICAiLCLilrMgICIsIuKWsyAgIiwi4pazICAiLCLilrMgICIsIuKWsyAgIiwi4pazICAiLCLilrMgICIsIuKWsyAgIiwi4pazICAiLCLilrMgICIsIuKWsyAgIiwi4pazICAiLCLiiJogICIsIuKImiAgIiwi4oiaICAiLCLiiJogICIsIuKIpyAgIiwiICAgICJdXSwiRGF0YUtleXMiOltbbnVsbF1dLCJTdGF0ZXMiOltbXV19fX0=
//                            X_TARGET=Grid1_Toolbar1_query_PaiKe
//                    __EVENTARGUMENT=
//                            __EVENTTARGET=Grid1$Toolbar1$query_PaiKe
//                    __VIEWSTATE=/wEPDwUKLTY0NjUxODI2MWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgYFBUdyaWQxBRpHcmlkMSRUb29sYmFyMSRkZGxfWHVlWXVhbgUYR3JpZDEkVG9vbGJhcjEkZGRsX0JhbkppBRpHcmlkMSRUb29sYmFyMSRxdWVyeV9QYWlLZQUaR3JpZDEkVG9vbGJhcjEkcHJpbnRfUGFpS2UFBUdyaWQy9EAoDHQdtDQOKcXI0dh/ynDMTUmpMgxvOu2w42XnwM4=
                    params = new HashMap();
                    params.put("Grid1$Toolbar1$ddl_BanJi", "104800");
                    params.put("Grid1$Toolbar1$ddl_XueYuan", "12");
                    params.put("X_STATE", "eyJHcmlkMV9Ub29sYmFyMV9kZGxfWHVlWXVhbiI6eyJEYXRhVGV4dEZpZWxkIjoiamdtYyIsIkRhdGFWYWx1ZUZpZWxkIjoiamdkbSIsIlhfSXRlbXMiOltbIjAyICAiLCIwNOacuueUteW3peeoi");
                    params.put("__VIEWSTATE", "/wEPDwUKLTY0NjUxODI2MWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgYFBUdyaWQxBRpHcmlkMSRUb29sYmFyMSRkZGxfWHVlWXVhbgUYR3JpZDEkVG9vbGJhcjEkZGRsX0JhbkppBRpHcmlkMSRUb29sYmFyMSRxdWVyeV9QYWlLZQUaR3JpZDEkVG9vbGJhcjEkcHJpbnRfUGFpS2UFBUdyaWQy9EAoDHQdtDQOKcXI0dh/ynDMTUmpMgxvOu2w42XnwM4=");
                    params.put("X_AJAX", "true");
//                    Document doc = Jsoup.connect(GSHNets.NET_EAS_COURSE)
//                            .userAgent("Mozilla")
//                            .method(Connection.Method.GET)
//                            .header("ASP.NET_SessionId",cookies.get("ASP.NET_SessionId"))
//                            .header("XingMing",cookies.get("XingMing"))
//                            .data(params)
//                            .cookies(cookies)
//                            .timeout(3000)
//                            .followRedirects(true)
//                            .get();
//                    logInfo = doc.toString();


                    Connection.Response res = Jsoup.connect(GSHNets.NET_EAS_COURSE)
                            .method(Connection.Method.GET)
                            .header("ASP.NET_SessionId",cookies.get("ASP.NET_SessionId"))
                            .header("XingMing",cookies.get("XingMing"))
                            .cookies(cookies)
                            .data(params)
                            .followRedirects(true)
                            .timeout(30000)
                            .execute();
                    logInfo=res.body();
                    runOnUiThread(tipRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 解析下拉列表内容
     *
     * @param flag 0：解析学院下拉列表内容，1：解析班级下拉列表内容
     * @return
     */
    private Bundle getSpinnerInfos(int flag, boolean needSelectedValue) {
        Document doc = null;
        try {
            doc = Jsoup.connect("http://218.15.22.136:3008/PaiKeXiTong.aspx")
                    .userAgent("Mozilla")
                    .cookies(cookies)
                    .timeout(3000)
                    .get();
        } catch (IOException e) {
            Log.i(TAG, "获取课程信息出现异常");
            return null;
        }
        if (null == doc) return null;

        String spinnerDoc = doc.toString();
        Bundle bundle = new Bundle();

        int tempStart = 0;
        if (flag == 1) {//如果是解析班级数据的话要多定位一遍ComboBox
            tempStart = spinnerDoc.indexOf("ComboBox") + 8;
        }
        tempStart = spinnerDoc.indexOf("ComboBox", tempStart);
        tempStart = spinnerDoc.indexOf("X_Items", tempStart);
        tempStart = spinnerDoc.indexOf("[", tempStart);
        int tempEnd = spinnerDoc.indexOf("]]", tempStart) + 2;
        String X_Items_Str = spinnerDoc.substring(tempStart, tempEnd);//截取出JsonArray类型String数据

        JSONArray jarr = null;
        try {
            jarr = new JSONArray(X_Items_Str);

            int len = jarr.length();
            ArrayList<SpinnerInfo> X_Items_ListArr = new ArrayList<SpinnerInfo>(len);
            for (int i = 0; i < len; i++) {
                JSONArray subJarr = jarr.optJSONArray(i);
                SpinnerInfo item = new SpinnerInfo();
                item.setFlagStr(subJarr.optString(0));
                item.setName(subJarr.optString(1));
                item.setFlagInt(subJarr.optInt(2));
                X_Items_ListArr.add(item);
            }
            bundle.putParcelableArrayList("spininfolist", X_Items_ListArr);

            logInfo = jarr.toString();
            runOnUiThread(tipRunnable);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        if (needSelectedValue) {//是否需要截取SelectedValue数据
            tempStart = 0;
            if (flag == 1) {
                tempStart = spinnerDoc.indexOf("SelectedValue") + 13;
            }
            tempStart = spinnerDoc.indexOf("SelectedValue", tempStart);
            tempStart = spinnerDoc.indexOf(":", tempStart) + 1;
            tempEnd = spinnerDoc.indexOf("}", tempStart);
            String SelectedValue = spinnerDoc.substring(tempStart, tempEnd).replace("\"", "");
            bundle.putString("SelectedValue", SelectedValue);
        }
        return bundle;
    }

    //获取成绩数据
    private void getScores() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                try {
                    doc = Jsoup.connect("http://218.15.22.136:3008/ChengJiChaXun.aspx")
                            .userAgent("Mozilla")
                            .cookies(cookies)
                            .timeout(3000)
                            .get();
                } catch (IOException ioe) {
                    Log.i(TAG, "获取成绩信息出现异常");
                    return;
                }

                logInfo = doc.toString();
                int indx = logInfo.indexOf("RecordCount");
                indx = logInfo.indexOf("Values", indx);
                indx = logInfo.indexOf("[[", indx);
                int ed = logInfo.indexOf("]]") + 2;
                logInfo = logInfo.substring(indx, ed);//将完整的成绩内容截取出来再做解析
                //完整数据格式:[[xxx,xxx],[yyy,yyy]]

                try {
                    JSONArray jarr = new JSONArray(logInfo);
                    int len = jarr.length();
                    List<Scores> scoresList = new ArrayList<Scores>(len);
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
                    logInfo = scoresList.toString();
                    runOnUiThread(tipRunnable);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i(TAG, "json获取成绩信息出现异常:" + e.getMessage());
                    return;
                }
                Log.i(TAG, "成绩信息doc=" + doc.toString());
            }
        }).start();
    }

    //获取个人数据
    private void getProfile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
//                    Connection.Response res = Jsoup.connect("http://218.15.22.136:3008/user.aspx")
//                            .cookies(cookies).timeout(30000).execute();

                    Document doc = Jsoup.connect("http://218.15.22.136:3008/user.aspx")
                            .userAgent("Mozilla")
                            .cookies(cookies)
                            .timeout(3000)
                            .get();

                    logInfo = doc.toString();
                    runOnUiThread(tipRunnable);

                    String[] names = new String[]{
                            "ImageUrl", "Form2$FormRow1$txt_XingMing",
                            "Form2$FormRow1$txt_XueHao", "Form2$FormRow1$txt_XingBie",
                            "Form2$FormRow2$txt_BanJi", "Form2$FormRow2$txt_XueZhi",
                            "Form2$FormRow2$txt_RuXueNianJi", "Form2$FormRow3$txt_YouXiang",
                            "Form2$FormRow3$txt_MinZu", "Form2$FormRow3$txt_ZhengZhiMianMao",
                            "Form2$FormRow4$txt_ChuShengRiQi", "Form2$FormRow4$txt_RuXueXingShi",
                            "Form2$FormRow4$txt_KaoShengLeiBie", "Form2$FormRow5$txt_ShenFengZheng",
                            "Form2$FormRow5$txt_JiaTingZhuZhi"};

                    int len = names.length;
                    String[] values = new String[len];
                    StringBuffer buffer = new StringBuffer();
                    EASProfile easp = new EASProfile();

                    String docString = doc.toString();

                    int indx = docString.indexOf(names[0]);
                    indx = docString.indexOf(":", indx) + 1;
                    int ed = docString.indexOf("}", indx);
                    String xm = docString.substring(indx, ed).replace("\"", "");
                    values[0] = xm;
                    buffer.append(xm).append(";");
                    easp.setZhaopian(xm);

                    for (int i = 1; i < len; i++) {
                        int index = docString.indexOf(names[i]);
                        index = docString.indexOf(":", index) + 1;
                        int end = docString.indexOf("}", index);
                        String xingming = docString.substring(index, end).replace("\"", "").trim();
                        values[i] = xingming;
                        buffer.append(values[i]).append(";");
                    }
                    easp.setXingming(values[1]);
                    easp.setXuehao(values[2]);
                    easp.setXingbie(values[3]);
                    easp.setBanji(values[4]);
                    easp.setXuezhi(values[5]);
                    easp.setRuxuenianji(values[6]);
                    easp.setYouxiang(values[7]);
                    easp.setMinzu(values[8]);
                    easp.setZhengzhimianmao(values[9]);
                    easp.setChushengriqi(values[10]);
                    easp.setRuxuexingshi(values[11]);
                    easp.setKaoshengleibie(values[12]);
                    easp.setShenfenzhenghao(values[13]);
                    easp.setJiatingzhuzhi(values[14]);

                    logInfo = easp.toString();
                    runOnUiThread(tipRunnable);

                } catch (IOException ioe) {
                    Log.i(TAG, "获取个人信息出现异常");
                }
            }
        }).start();
    }

    private void getData() {
        params = new HashMap();
        params.put("Grid1$Toolbar1$ddl_XueYuan", "02");
        params.put("Grid1$Toolbar1$ddl_BanJi", "0");
        params.put("__EVENTTARGET", "Grid1$Toolbar1$ddl_XueYuan");
        params.put("__VIEWSTATE", "/wEPDwUKLTY0NjUxODI2MWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgYFBUdyaWQxBRpHcmlkMSRUb29sYmFyMSRkZGxfWHVlWXVhbgUYR3JpZDEkVG9vbGJhcjEkZGRsX0JhbkppBRpHcmlkMSRUb29sYmFyMSRxdWVyeV9QYWlLZQUaR3JpZDEkVG9vbGJhcjEkcHJpbnRfUGFpS2UFBUdyaWQy9EAoDHQdtDQOKcXI0dh/ynDMTUmpMgxvOu2w42XnwM4=");
        StringRequest sr = new StringRequest(Request.Method.POST, "http://218.15.22.136:3008/PaiKeXiTong.aspx", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i(TAG, "getData-s=" + s);
                logInfo = s;
                runOnUiThread(tipRunnable);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i(TAG, "getData失败：" + volleyError.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return cookies;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        Volley.newRequestQueue(this).add(sr);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.aaa:
                login();
                break;
            case R.id.bbb:
                getScores();
                break;
            case R.id.ccc:
//                getCourses();
                newGetCourses();
                break;
            case R.id.ddd:
                getProfile();
                break;
            case R.id.selectyuanxi:
                updateBanjiSpinnerItems("");
//                getData();
                break;
        }
    }

    //    设置提示信息
    Runnable tipRunnable = new Runnable() {
        @Override
        public void run() {
            tip.setText(logInfo);
        }
    };

    //    设置院系下拉列表的数据
    Runnable xueyuanRunnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<SpinnerInfo> tempList = bundle.getParcelableArrayList("spininfolist");
            xueyuanAdapter.setList(tempList);

            String SelectedValue = bundle.getString("SelectedValue").trim();
            if (null != SelectedValue) {
                tip.setText(SelectedValue);
                int len = tempList.size();
                for (int i = 0; i < len; i++) {
                    SpinnerInfo tempInfo = tempList.get(i);
                    if (SelectedValue.equals(tempInfo.getFlagStr().trim())) {
                        xueyuan.setSelection(i);
                        break;
                    }
                }
            }
        }
    };

    //    设置班级下拉列表的数据
    Runnable banjiRunnable = new Runnable() {
        @Override
        public void run() {
            ArrayList<SpinnerInfo> tempList = bundle.getParcelableArrayList("spininfolist");
            banjiAdapter.setList(tempList);

            String SelectedValue = bundle.getString("SelectedValue");
            if (null != SelectedValue) {
                tip.setText(SelectedValue);
                int len = tempList.size();
                for (int i = 0; i < len; i++) {
                    SpinnerInfo tempInfo = tempList.get(i);
                    if (SelectedValue.trim().equals(tempInfo.getFlagStr().trim())) {
                        banji.setSelection(i);
                        break;
                    }
                }
            }
        }
    };

    //    更新班级下拉列表数据
    private void updateBanjiSpinnerItems(String xueyuan) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Document doc = null;
                params = new HashMap();
                params.put("Grid1$Toolbar1$ddl_XueYuan", "02  ");
                params.put("Grid1$Toolbar1$ddl_BanJi", "5100");
                params.put("__EVENTARGUMENT", "");
                params.put("__EVENTTARGET", "Grid1$Toolbar1$ddl_XueYuan");
                params.put("__VIEWSTATE", "/wEPDwUKLTY0NjUxODI2MWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgYFBUdyaWQxBRpHcmlkMSRUb29sYmFy\n" +
                        "MSRkZGxfWHVlWXVhbgUYR3JpZDEkVG9vbGJhcjEkZGRsX0JhbkppBRpHcmlkMSRUb29sYmFyMSRxdWVyeV9QYWlLZQUaR3JpZDEkVG9vbGJhcjEkcHJpbnRfUGFpS2UFBUdyaWQy9EAoDHQdtDQOKcXI0dh\n" +
                        "/ynDMTUmpMgxvOu2w42XnwM4=");


                try {
//                    doc = Jsoup.connect("http://218.15.22.136:3008/PaiKeXiTong.aspx")
//                            .userAgent("Mozilla")
//                            .cookies(cookies)
//                            .data(params)
//                            .timeout(3000)
//                            .header("User-Agent",
//                                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36")
//                            .post();
//                    logInfo = doc.toString();

                    Connection.Response res = Jsoup.connect("http://218.15.22.136:3008/PaiKeXiTong.aspx")
                            .data(params)
                            .cookies(cookies)
                            .timeout(30000)
                            .execute();
                    logInfo = res.body();

                    Log.i(TAG, "response body=" + logInfo);
                    runOnUiThread(tipRunnable);
                } catch (IOException e) {
                    Log.i(TAG, "获取课程信息出现异常");
                }
            }
        }).start();
    }
}
