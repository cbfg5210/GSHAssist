package com.hawk.gshassist.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.hawk.gshassist.R;
import com.hawk.gshassist.util.AppFragmentManager;
import com.hawk.gshassist.util.GSHNets;
import com.hawk.gshassist.util.ToastUtil;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends BaseFragment implements OnClickListener {
    private static final String TAG = "LoginFragment";
    private EditText flon_acct, flon_pwd;
    private Button flon_login;
    private int flag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        flag = arguments.getInt("flag");
//        Log.i(TAG,"url="+url+";flag="+flag);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_course for this fragment
        View layoutView = inflater.inflate(R.layout.fragment_login, null);

        flon_acct = (EditText) layoutView.findViewById(R.id.flon_acct);
        flon_pwd = (EditText) layoutView.findViewById(R.id.flon_pwd);
        flon_login = (Button) layoutView.findViewById(R.id.flon_login);

        flon_login.setOnClickListener(this);

        return layoutView;
    }

    @Override
    public void onClick(View v) {
        String acct = flon_acct.getText().toString();
        String pwd = flon_pwd.getText().toString();

        if (TextUtils.isEmpty(acct)) {
            ToastUtil.show("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            ToastUtil.show("请输入密码");
            return;
        }

        login(acct, pwd);
    }

    private void login(final String acct, final String pwd) {
        mListener.showProgress(null,"正在验证信息,请稍候...");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> params;
                String url;
                if (flag == 0){
                    url= GSHNets.NET_EAS_LOGIN;
                    params = getEASLoginParams(acct, pwd);
                }
                else{
                    url= GSHNets.NET_LIB_LOGIN;
                    params = getLibLoginParams(acct, pwd);
                }

                try {
                    Connection.Response res = Jsoup.connect(url)
                            .data(params)
                            .timeout(30000)
                            .method(Connection.Method.GET)
                            .followRedirects(false)
                            .execute();
                    Map cookies = res.cookies();
                    Log.i(TAG, "cookie=" + cookies);

                    if (null == cookies || cookies.isEmpty()) {
                        showLoginStatTip("登录失败，没有cookie");
                        return;
                    }
                    mListener.dismissProgress();
                    showLoginStatTip("登录成功");
                    mListener.setCookies(cookies, flag);
                    if (flag == 0) {
                        mListener.switchToFragment(AppFragmentManager.EASPROFILEFRAGMENT, null,true);
                    } else {
                        mListener.switchToFragment(AppFragmentManager.LIBPROFILEFRAGMENT,null,true);
                    }
                } catch (IOException ioe) {
                    Log.i(TAG, "exception");
                    mListener.dismissProgress();
                    showLoginStatTip("登录失败:" + ioe.getMessage());
                }
            }
        }).start();
    }

    /**
     * 返回教务系统登录要用到的参数
     *
     * @param acct
     * @param pwd
     * @return
     */
    private Map getEASLoginParams(String acct, String pwd) {
        Map<String, String> params = new HashMap<>();

        params.put("Window1$SimpleForm1$rdl_shenFen", "学生");
        params.put("Window1$SimpleForm1$tbx_XueHao", acct);
        params.put("Window1$SimpleForm1$tbx_pwd", pwd);
        //需要添加以下两个参数
        params.put("__EVENTTARGET", "Window1$Toolbar1$btn_login");
        params.put("__VIEWSTATE",
                "/wEPDwULLTE5MDM2NzU3OTVkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYGBQdXaW5kb3cxBRNXaW5kb3cxJFNpbXBsZUZvcm0xBR5XaW5kb3cxJFNpbXBsZUZvcm0xJHRieF9YdWVIYW8FG1dpbmRvdzEkU2ltcGxlRm9ybTEkdGJ4X3B3ZAUfV2luZG93MSRTaW1wbGVGb3JtMSRyZGxfc2hlbkZlbgUaV2luZG93MSRUb29sYmFyMSRidG5fbG9naW48JqvBYCN12yX7qJv0+Skqb/+OMwW2rhBjeJStRa2gMg==");

        return params;
    }

    private Map getLibLoginParams(String acct, String pwd) {
        Map<String, String> params = new HashMap<>();
        //从健壮性方面考虑的话，应该先从网页取下__EVENTVALIDATION和__VIEWSTATE这两个参数而不是写死
        params.put("__EVENTVALIDATION", "/wEWBQLL8qjnAwLxkMjADwLfkqekBgLN05+fBgKe9OnfBp7D99mVwyohC+CBN1UjLr2g1ekC");
        params.put("__VIEWSTATE", "/wEPDwUJNzk4Mzg2Mzc2D2QWAgIDD2QWBAIDD2QWBGYPD2QWAh4MYXV0b2NvbXBsZXRlBQNvZmZkAgMPDxYCHgRUZXh0ZWRkAgUPZBYGZg8QZGQWAWZkAgEPD2QWAh8ABQNvZmZkAggPEGRkFgFmZGRY6YpBLgJFORuy9HZhigk2TE0Tzg==");
        params.put("txtUsername_Lib", "r68771");
        params.put("txtPas_Lib", "68771");
        params.put("btnLogin_Lib", "登录");
        return params;
    }

    //显示登录状态
    private void showLoginStatTip(final String msg) {
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    public void refreshFragment() {
        flon_login.performClick();
    }
}
