package com.hawk.gshassist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.hawk.gshassist.R;
import com.hawk.gshassist.util.GSHNets;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class TestApi_course extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestApi";
    private Map<String, String> params;
    private Map<String, String> cookies;
    private Button aaa, ccc;
    private EditText tip;
    private String logInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_testapi_course);

        aaa = (Button) findViewById(R.id.aaa);//登录
        ccc = (Button) findViewById(R.id.ccc);//课程
        tip = (EditText) findViewById(R.id.tip);

        aaa.setOnClickListener(this);
        ccc.setOnClickListener(this);
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
                try {
                    Document doc = Jsoup.connect("http://218.15.22.136:3008/PaiKeXiTong.aspx")
                            .userAgent("Mozilla")
                            .cookies(cookies)
                            .timeout(3000)
                            .get();
                    logInfo=doc.toString();
                    runOnUiThread(tipRunnable);
                } catch (IOException e) {
                    Log.i(TAG, "获取课程信息出现异常");
                }
            }
        }).start();
    }

    private void newGetCourses() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    params = new HashMap();
                    params.put("Grid1$Toolbar1$ddl_BanJi", "104800");
                    params.put("Grid1$Toolbar1$ddl_XueYuan", "12  ");
                    params.put("X_STATE", "eyJHcmlkMV9Ub29sYmFyMV9kZGxfWHVlWXVhbiI6eyJEYXRhVGV4dEZpZWxkIjoiamdtYyIsIkRhdGFWYWx1ZUZpZWxkIjoiamdkbSIsIlhfSXRlbXMiOltbIjAyICAiLCIwNOacuueUteW3peeoi");
                    params.put("__VIEWSTATE", "/wEPDwUKLTY0NjUxODI2MWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgYFBUdyaWQxBRpHcmlkMSRUb29sYmFyMSRkZGxfWHVlWXVhbgUYR3JpZDEkVG9vbGJhcjEkZGRsX0JhbkppBRpHcmlkMSRUb29sYmFyMSRxdWVyeV9QYWlLZQUaR3JpZDEkVG9vbGJhcjEkcHJpbnRfUGFpS2UFBUdyaWQy9EAoDHQdtDQOKcXI0dh/ynDMTUmpMgxvOu2w42XnwM4=");
//                    params.put("X_AJAX", "true");
//                    Document doc = Jsoup.connect(GSHNets.NET_EAS_COURSE)
//                            .userAgent("Mozilla")
//                            .method(Connection.Method.GET)
////                            .header("ASP.NET_SessionId",cookies.get("ASP.NET_SessionId"))
////                            .header("XingMing",cookies.get("XingMing"))
//                            .data(params)
//                            .cookies(cookies)
//                            .timeout(3000)
//                            .followRedirects(false)
//                            .get();
//                    logInfo = doc.toString();


                    Connection.Response res = Jsoup.connect(GSHNets.NET_EAS_COURSE)
//                            .method(Connection.Method.GET)
//                            .header("ASP.NET_SessionId",cookies.get("ASP.NET_SessionId"))
//                            .header("XingMing",cookies.get("XingMing"))
                            .cookies(cookies)
                            .data(params)
//                            .followRedirects(false)
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

    private void getData() {
        params = new HashMap();
        params.put("Grid1$Toolbar1$ddl_BanJi", "104800");
        params.put("Grid1$Toolbar1$ddl_XueYuan", "12  ");
        params.put("X_STATE", "eyJHcmlkMV9Ub29sYmFyMV9kZGxfWHVlWXVhbiI6eyJEYXRhVGV4dEZpZWxkIjoiamdtYyIsIkRhdGFWYWx1ZUZpZWxkIjoiamdkbSIsIlhfSXRlbXMiOltbIjAyICAiLCIwNOacuueUteW3peeoi");
        params.put("__VIEWSTATE", "/wEPDwUKLTY0NjUxODI2MWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgYFBUdyaWQxBRpHcmlkMSRUb29sYmFyMSRkZGxfWHVlWXVhbgUYR3JpZDEkVG9vbGJhcjEkZGRsX0JhbkppBRpHcmlkMSRUb29sYmFyMSRxdWVyeV9QYWlLZQUaR3JpZDEkVG9vbGJhcjEkcHJpbnRfUGFpS2UFBUdyaWQy9EAoDHQdtDQOKcXI0dh/ynDMTUmpMgxvOu2w42XnwM4=");
        params.put("X_AJAX", "true");
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

    private void getCourseByUrlConn(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuffer paramBuffer=new StringBuffer();
                    paramBuffer.append("Grid1$Toolbar1$ddl_BanJi=104800&")
                            .append("Grid1$Toolbar1$ddl_XueYuan=12  ")
                            .append("&X_STATE=eyJHcmlkMV9Ub29sYmFyMV9kZGxfWHVlWXVhbiI6eyJEYXRhVGV4dEZpZWxkIjoiamdtYyIsIkRhdGFWYWx1ZUZpZWxkIjoiamdkbSIsIlhfSXRlbXMiOltbIjAyICAiLCIwNOacuueUteW3peeoi").append(cookies.get("XingMing"))
                            .append("&__VIEWSTATE=/wEPDwUKLTY0NjUxODI2MWQYAQUeX19Db250cm9sc1JlcXVpcmVQb3N0QmFja0tleV9fFgYFBUdyaWQxBRpHcmlkMSRUb29sYmFyMSRkZGxfWHVlWXVhbgUYR3JpZDEkVG9vbGJhcjEkZGRsX0JhbkppBRpHcmlkMSRUb29sYmFyMSRxdWVyeV9QYWlLZQUaR3JpZDEkVG9vbGJhcjEkcHJpbnRfUGFpS2UFBUdyaWQy9EAoDHQdtDQOKcXI0dh/ynDMTUmpMgxvOu2w42XnwM4=")
                            .append("&X_AJAX=true")
                    ;

                    //建立连接
                    URL url=new URL(GSHNets.NET_EAS_COURSE);
                    HttpURLConnection httpConn=(HttpURLConnection)url.openConnection();
                    //设置参数
                    httpConn.setDoOutput(true);   //需要输出
                    httpConn.setDoInput(true);   //需要输入
                    httpConn.setUseCaches(false);  //不允许缓存
                    httpConn.setRequestMethod("POST");   //设置POST方式连接
                    //设置请求属性
                    httpConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    httpConn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                    httpConn.setRequestProperty("Charset", "UTF-8");
                    httpConn.setRequestProperty("ASP.NET_SessionId",cookies.get("ASP.NET_SessionId"));
                    httpConn.setRequestProperty("XingMing",cookies.get("XingMing"));
                    //连接,也可以不用明文connect，使用下面的httpConn.getOutputStream()会自动connect
                    httpConn.connect();
                    //建立输入流，向指向的URL传入参数
                    DataOutputStream dos=new DataOutputStream(httpConn.getOutputStream());
                    dos.writeBytes(paramBuffer.toString());
                    dos.flush();
                    dos.close();
                    //获得响应状态
                    int resultCode=httpConn.getResponseCode();
                    if(HttpURLConnection.HTTP_OK==resultCode){
                        StringBuffer sb=new StringBuffer();
                        String readLine=new String();
                        BufferedReader responseReader=new BufferedReader(new InputStreamReader(httpConn.getInputStream(),"UTF-8"));
                        while((readLine=responseReader.readLine())!=null){
                            sb.append(readLine).append("\n");
                        }
                        responseReader.close();
                        logInfo=sb.toString();
                        runOnUiThread(tipRunnable);
                    }}catch (Exception e) {
                    Log.i(TAG,"出错啦："+e.getMessage());
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.aaa:
                login();
                break;
            case R.id.ccc:
                getCourses();
//                newGetCourses();
//                getData();
//                getCourseByUrlConn();
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
}
