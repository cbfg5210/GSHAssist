package com.hawk.gshassist.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.LibProfile;
import com.hawk.gshassist.util.GSHNets;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class LibProfileFragment extends BaseFragment {
    private static final String TAG="LibProfileFragment";
    private TextView flpre_zhenghao;
    private TextView flpre_xingming;
    private TextView flpre_leixing;
    private TextView flpre_danwei;
    private TextView flpre_dangqianzhuangtai;
    private TextView flpre_dianhua;
    private TextView flpre_shouji;
    private TextView flpre_beizhu;
    private TextView flpre_email;
    private TextView flpre_zhuzhi;
    private TextView flpre_qianfei;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutView=inflater.inflate(R.layout.fragment_lib_profile,null);
        flpre_zhenghao= (TextView) layoutView.findViewById(R.id.flpre_zhenghao);
        flpre_xingming= (TextView) layoutView.findViewById(R.id.flpre_xingming);
        flpre_leixing= (TextView) layoutView.findViewById(R.id.flpre_leixing);
        flpre_danwei= (TextView) layoutView.findViewById(R.id.flpre_danwei);
        flpre_dangqianzhuangtai= (TextView) layoutView.findViewById(R.id.flpre_dangqianzhuangtai);
        flpre_dianhua= (TextView) layoutView.findViewById(R.id.flpre_dianhua);
        flpre_shouji= (TextView) layoutView.findViewById(R.id.flpre_shouji);
        flpre_beizhu= (TextView) layoutView.findViewById(R.id.flpre_beizhu);
        flpre_email= (TextView) layoutView.findViewById(R.id.flpre_email);
        flpre_zhuzhi= (TextView) layoutView.findViewById(R.id.flpre_zhuzhi);
        flpre_qianfei= (TextView) layoutView.findViewById(R.id.flpre_qianfei);

        getProfile();

        return layoutView;
    }

    private void getProfile() {
        mListener.showProgress(null,"正在获取个人信息,请稍候...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Connection.Response rps = Jsoup.connect(GSHNets.NET_LIB_USER)
                            .cookies(mListener.getCookies(1))
                            .method(Connection.Method.GET)
                            .followRedirects(true)
                            .execute();
                    Document doc=rps.parse();

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

                    showProfile(libProfile);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i(TAG,"获取个人信息出错");
                    mListener.dismissProgress();
                }
            }
        }).start();
    }

    private void showProfile(final LibProfile profile){
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                flpre_zhenghao.setText(profile.getZhenghao());
                flpre_xingming.setText(profile.getXingming());
                flpre_leixing.setText(profile.getLeixing());
                flpre_danwei.setText(profile.getDanwei());
                flpre_dangqianzhuangtai.setText(profile.getDanqianzhuangtai());
                flpre_dianhua.setText(profile.getDianhua());
                flpre_shouji.setText(profile.getShouji());
                flpre_beizhu.setText(profile.getBeizhu());
                flpre_email.setText(profile.getEmaildizhi());
                flpre_zhuzhi.setText(profile.getZhuzhi());
                flpre_qianfei.setText(profile.getQianfei());
                mListener.dismissProgress();
            }
        });
    }

    @Override
    public void refreshFragment() {
        getProfile();
    }
}
