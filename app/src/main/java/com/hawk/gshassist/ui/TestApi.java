package com.hawk.gshassist.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.BookInfo;
import com.hawk.gshassist.model.CurBorrowInfo;
import com.hawk.gshassist.model.HisBorrowInfo;
import com.hawk.gshassist.model.LibProfile;
import com.hawk.gshassist.util.GSHNets;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestApi extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestApi";
    private EditText tip, ltei_keys;
    private Map cookies;
    private String tempInfo;
    private int hispageCount=-1;
    private int hiscurrentPage;
    private Bundle tempBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_testapi);

        Button ltei_login = (Button) findViewById(R.id.ltei_login);
        Button ltei_profile = (Button) findViewById(R.id.ltei_profile);
        Button ltei_curborrow = (Button) findViewById(R.id.ltei_curborrow);
        Button ltei_hisborrow = (Button) findViewById(R.id.ltei_hisborrow);
        Button ltei_search = (Button) findViewById(R.id.ltei_search);
        tip = (EditText) findViewById(R.id.tip);
        ltei_keys = (EditText) findViewById(R.id.ltei_keys);

        ltei_login.setOnClickListener(this);
        ltei_profile.setOnClickListener(this);
        ltei_curborrow.setOnClickListener(this);
        ltei_hisborrow.setOnClickListener(this);
        ltei_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        switch (vid) {
            case R.id.ltei_login:
                login();
                break;
            case R.id.ltei_profile:
                getProfile();
                break;
            case R.id.ltei_curborrow:
                getCurborrowBooks();
                break;
            case R.id.ltei_hisborrow:
                if(hiscurrentPage==0&&hispageCount==-1){
                    getHisborrowBooks(0,true);
                }else{
                    if(hiscurrentPage<=hispageCount){
                        getHisborrowBooks(hiscurrentPage,false);
                    }
                }
                break;
            case R.id.ltei_search:
                searchBooks();
                break;
            default:;
        }
    }

    private void searchBooks() {
        String keywords = ltei_keys.getText().toString();
        final String url = GSHNets.NET_LIB_SEARCHBOOK.replace("@keywords@", keywords);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response rps = Jsoup.connect(url)
                            .method(Connection.Method.GET)
                            .followRedirects(true)
                            .execute();

                    tempInfo = rps.body();
                    runOnUiThread(tipRunnable);

                    List<BookInfo> booksList=new ArrayList<BookInfo>();
                    Document doc=rps.parse();
                    Element elem=doc.select("tbody").first();
                    Elements trs=elem.select("tr");
                    for(int i=0;i<trs.size();i++){
                        Element tr=trs.get(i);
                        Elements tds=tr.select("td");

                        BookInfo item=new BookInfo();
                        item.setTiming(tds.get(1).text());
                        item.setZerenzhe(tds.get(2).text());
                        item.setChubanshe(tds.get(3).text());
                        item.setChubannian(tds.get(4).text());
                        item.setSuoquhao(tds.get(5).text());
                        item.setGuancang(tds.get(6).text());
                        item.setKejie(tds.get(7).text());
                        item.setXiangguanziyuan(tds.get(8).text());

                        booksList.add(item);
                    }
                    tempInfo=booksList.toString();
                    runOnUiThread(tipRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //因为当前借阅书籍数量有限制，所以可以一次返回全部
    private void getCurborrowBooks() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect("http://210.38.245.168:81//user/bookborrowed.aspx")
                            .cookies(cookies).get();

                    ArrayList<CurBorrowInfo>curborrowList=new ArrayList<CurBorrowInfo>();

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
                    tempInfo=curborrowList.toString();
                    runOnUiThread(tipRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 获取第某页数据
     * @param page
     * @param withPageCount 是否返回页数
     */
    private void getHisborrowBooks(final int page, final boolean withPageCount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url="http://210.38.245.168:81/user/bookborrowedhistory.aspx?page="+page;
                try {
                    Response rps = Jsoup.connect(url)
                            .cookies(cookies)
                            .method(Connection.Method.GET)
                            .followRedirects(true)
                            .execute();

                    Document doc=rps.parse();
                    if(TextUtils.isEmpty(doc.toString())) return;
                    tempBundle=new Bundle();

//                    tempInfo=doc.toString();
//                    runOnUiThread(tipRunnable);
//                    return;

                    if(withPageCount){
                        Element pageCount=doc.select("#ctl00_cpRight_Pagination1_gplbl2").first();
                        if(null==pageCount)return;
                        try{
                            int pagecount=Integer.parseInt(pageCount.text().trim());  //!!!!当没有历史借阅信息时,pageCount.text()的值应该是什么?
                            tempBundle.putInt("pagecount",pagecount);

                            if(pagecount==0)return;//没有数据的话就不用执行下面的解析了
                        }catch (NumberFormatException nfe){
                            Log.i(TAG,"数据解析出现异常，在转换pagecount为int型时出现异常");
                            return;
                        }
                    }

                    ArrayList<HisBorrowInfo>hisborrowList=new ArrayList<HisBorrowInfo>();
                    Element subinfolines = doc.select("tbody").first();
                    Elements subtrs=subinfolines.select("tr");
                    int len=subtrs.size();
                    for(int j=0;j<len;j++){
                        HisBorrowInfo item=new HisBorrowInfo();
                        Element element=subtrs.get(j);
                        Elements tds=element.select("td"); //td

                        item.setJieqi(tds.get(0).text());
                        item.setHuanqi(tds.get(1).text());
                        item.setTiming(tds.get(2).text());
                        item.setTushuleixing(tds.get(3).text());
                        item.setDengluhao(tds.get(4).text());

                        hisborrowList.add(item);
                    }
                    tempBundle.putParcelableArrayList("hisborrowList",hisborrowList);

                    hiscurrentPage++;

                    runOnUiThread(hisborrowRunnable);

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG,"解析历史借阅数据出现异常:"+e.getMessage());
                }
            }
        }).start();
    }

    private void getProfile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response rps = Jsoup.connect("http://210.38.245.168:81/user/userinfo.aspx")
                            .cookies(cookies)
                            .method(Connection.Method.GET)
                            .followRedirects(true)
                            .execute();
                    Document doc=rps.parse();

                    Element userInfoContent=doc.getElementById("div#userInfoContent");
                    Elements infolines=doc.getElementsByClass("infoline");
                    int len=infolines.size()-1;
                    infolines.remove(len);
                    String[]infos=new String[len];
                    String tempString;
                    StringBuffer buffer=new StringBuffer();
                    for(int i=0;i<len;i++){
                        Element infoline=infolines.get(i);
                        tempString=infoline.getElementsByClass("infoleft").first().text();
                        tempString+=infoline.getElementsByClass("inforight").first().text();
                        infos[i]=tempString;
                        buffer.append(tempString).append("\n");
                    }
                    LibProfile libProfile=new LibProfile();
                    libProfile.setZhenghao(infos[0]);
                    libProfile.setXingming(infos[1]);
                    libProfile.setLeixing(infos[2]);
                    libProfile.setDanwei(infos[3]);
                    libProfile.setDanqianzhuangtai(infos[4]);
                    libProfile.setDianhua(infos[5]);
                    libProfile.setShouji(infos[6]);
                    libProfile.setBeizhu(infos[7]);
                    libProfile.setEmaildizhi(infos[8]);
                    libProfile.setZhuzhi(infos[9]);
                    libProfile.setQianfei(infos[10]);

                    //id:userInfoContent-class:infoline -class:infoleft
                    //                                  -class:inforight

                    tempInfo=buffer.toString();
                    runOnUiThread(tipRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG,"获取个人信息出错");
                }
            }
        }).start();
    }

    private void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map params = new HashMap<String, String>();
                //从健壮性方面考虑的话，应该先从网页取下__EVENTVALIDATION和__VIEWSTATE这两个参数而不是写死
                params.put("__EVENTVALIDATION", "/wEWBQLL8qjnAwLxkMjADwLfkqekBgLN05+fBgKe9OnfBp7D99mVwyohC+CBN1UjLr2g1ekC");
                params.put("__VIEWSTATE", "/wEPDwUJNzk4Mzg2Mzc2D2QWAgIDD2QWBAIDD2QWBGYPD2QWAh4MYXV0b2NvbXBsZXRlBQNvZmZkAgMPDxYCHgRUZXh0ZWRkAgUPZBYGZg8QZGQWAWZkAgEPD2QWAh8ABQNvZmZkAggPEGRkFgFmZGRY6YpBLgJFORuy9HZhigk2TE0Tzg==");
                params.put("txtUsername_Lib", "r68771");
                params.put("txtPas_Lib", "68771");
                params.put("btnLogin_Lib", "登录");

                try {
                    Response rps = Jsoup
                            .connect("http://210.38.245.168:81//test1.aspx")
                            .data(params)
                            .method(Connection.Method.GET)
                            .followRedirects(false)
                            .execute();
                    cookies = rps.cookies();

                    tempInfo = cookies.toString();
                    runOnUiThread(tipRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                    tempInfo = "登录图书馆失败:" + e.getMessage();
                    runOnUiThread(tipRunnable);
                }
            }
        }).start();
    }

    Runnable tipRunnable = new Runnable() {
        @Override
        public void run() {
            tip.setText(tempInfo);
        }
    };
    Runnable hisborrowRunnable=new Runnable() {
        @Override
        public void run() {
            if(tempBundle==null)return;
            int pagecount=tempBundle.getInt("pagecount",-1);
            if(pagecount!=-1)hispageCount=pagecount;
            ArrayList<HisBorrowInfo>hisborrowList=tempBundle.getParcelableArrayList("hisborrowList");
            tempInfo=hisborrowList.toString();
            tip.setText(tempInfo);
        }
    };
}
