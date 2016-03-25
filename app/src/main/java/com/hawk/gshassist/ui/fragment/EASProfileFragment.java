package com.hawk.gshassist.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hawk.gshassist.R;
import com.hawk.gshassist.model.EASProfile;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class EASProfileFragment extends BaseFragment {
    private static final String TAG = "EASProfileFragment";
    private TextView fepre_zhaopian, fepre_xingming, fepre_banji, fepre_youxiang, fepre_chushengriqi;
    private TextView fepre_shenfenzhenghao, fepre_xuehao, fepre_xuezhi, fepre_minzu, fepre_ruxuexingshi;
    private TextView fepre_xingbie, fepre_ruxuenianji, fepre_zhengzhimianmao, fepre_kaoshengleibie, fepre_jiatingzhuzhi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_course for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_eas_profile, null);

        fepre_zhaopian = (TextView) layoutView.findViewById(R.id.fepre_zhaopian);
        fepre_xingming = (TextView) layoutView.findViewById(R.id.fepre_xingming);
        fepre_banji = (TextView) layoutView.findViewById(R.id.fepre_banji);
        fepre_youxiang = (TextView) layoutView.findViewById(R.id.fepre_youxiang);
        fepre_chushengriqi = (TextView) layoutView.findViewById(R.id.fepre_chushengriqi);
        fepre_shenfenzhenghao = (TextView) layoutView.findViewById(R.id.fepre_shenfenzhenghao);
        fepre_xuehao = (TextView) layoutView.findViewById(R.id.fepre_xuehao);
        fepre_xuezhi = (TextView) layoutView.findViewById(R.id.fepre_xuezhi);
        fepre_minzu = (TextView) layoutView.findViewById(R.id.fepre_minzu);
        fepre_ruxuexingshi = (TextView) layoutView.findViewById(R.id.fepre_ruxuexingshi);
        fepre_xingbie = (TextView) layoutView.findViewById(R.id.fepre_xingbie);
        fepre_ruxuenianji = (TextView) layoutView.findViewById(R.id.fepre_ruxuenianji);
        fepre_zhengzhimianmao = (TextView) layoutView.findViewById(R.id.fepre_zhengzhimianmao);
        fepre_kaoshengleibie = (TextView) layoutView.findViewById(R.id.fepre_kaoshengleibie);
        fepre_jiatingzhuzhi = (TextView) layoutView.findViewById(R.id.fepre_jiatingzhuzhi);

        getProfile();

        return layoutView;
    }

    //获取个人数据
    private void getProfile() {
        mListener.showProgress(null,"正在获取个人信息,请稍等...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(GSHNets.NET_EAS_USER)
                            .userAgent("Mozilla")
                            .cookies(mListener.getCookies(0))
                            .timeout(3000)
                            .get();

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
                    EASProfile easp = new EASProfile();

                    String docString = doc.toString();

                    int indx = docString.indexOf(names[0]);
                    indx = docString.indexOf(":", indx) + 1;
                    int ed = docString.indexOf("}", indx);
                    String touxiang = docString.substring(indx, ed).replace("\"", "");
                    values[0] = touxiang;
                    easp.setZhaopian(touxiang);

                    for (int i = 1; i < len; i++) {
                        int index = docString.indexOf(names[i]);
                        index = docString.indexOf(":", index) + 1;
                        int end = docString.indexOf("}", index);
                        String tempStr = docString.substring(index, end).replace("\"", "").trim();
                        values[i] = tempStr;
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

                    showProfile(easp);
                } catch (IOException ioe) {
                    Log.i(TAG, "获取个人信息出现异常");
                    mListener.dismissProgress();
                    ToastUtil.show("获取数据失败");
                }
            }
        }).start();
    }

    private void showProfile(final EASProfile profile){
            getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fepre_zhaopian.setText(profile.getZhaopian());
                fepre_xingming.setText(profile.getXingming());
                fepre_xuehao.setText(profile.getXuehao());
                fepre_xingbie.setText(profile.getXingbie());
                fepre_banji.setText(profile.getBanji());
                fepre_xuezhi.setText(profile.getXuezhi());
                fepre_ruxuenianji.setText(profile.getRuxuenianji());
                fepre_youxiang.setText(profile.getYouxiang());
                fepre_minzu.setText(profile.getMinzu());
                fepre_zhengzhimianmao.setText(profile.getZhengzhimianmao());
                fepre_chushengriqi.setText(profile.getChushengriqi());
                fepre_ruxuexingshi.setText(profile.getRuxuexingshi());
                fepre_kaoshengleibie.setText(profile.getKaoshengleibie());
                fepre_shenfenzhenghao.setText(profile.getShenfenzhenghao());
                fepre_jiatingzhuzhi.setText(profile.getJiatingzhuzhi());
                mListener.dismissProgress();
            }
        });
    }

    @Override
    public void refreshFragment() {
        getProfile();
    }
}
