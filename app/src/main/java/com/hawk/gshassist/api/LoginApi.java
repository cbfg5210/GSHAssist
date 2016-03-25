package com.hawk.gshassist.api;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * 在此写用途
 * Created by hawk on 2016/3/17.
 */
public class LoginApi {
    private static Map EASCookie;
    private static Map LibCookie;

    public static boolean loginToEAS(String url, Map params){
        try{
            Connection.Response res = Jsoup.connect(url)
                    .data(params).timeout(30000).execute();
            EASCookie = res.cookies();
            if(null==EASCookie||EASCookie.isEmpty()){
                return false;
            }
        }catch (IOException ioe){
            return false;
        }
        return true;
    }

    public static boolean loginToLib(String url, Map params){
        try{
            Connection.Response res = Jsoup.connect(url)
                    .data(params).timeout(30000).execute();
            LibCookie = res.cookies();
            if(null==LibCookie||LibCookie.isEmpty()){
                return false;
            }
        }catch (IOException ioe){
            return false;
        }
        return true;
    }

    /*public static boolean login(){
        Map<String, String> params = new HashMap<String, String>();
        params.put("Window1$SimpleForm1$rdl_shenFen", "学生");
        params.put("Window1$SimpleForm1$tbx_XueHao", "12124640117");
        params.put("Window1$SimpleForm1$tbx_pwd", "71104642121");
        //需要添加以下两个参数
        params.put("__EVENTTARGET", "Window1$Toolbar1$btn_login");
        params.put("__VIEWSTATE",
                "/wEPDwULLTE5MDM2NzU3OTVkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2tLZXlfXxYGBQdXaW5kb3cxBRNXaW5kb3cxJFNpbXBsZUZvcm0xBR5XaW5kb3cxJFNpbXBsZUZvcm0xJHRieF9YdWVIYW8FG1dpbmRvdzEkU2ltcGxlRm9ybTEkdGJ4X3B3ZAUfV2luZG93MSRTaW1wbGVGb3JtMSRyZGxfc2hlbkZlbgUaV2luZG93MSRUb29sYmFyMSRidG5fbG9naW48JqvBYCN12yX7qJv0+Skqb/+OMwW2rhBjeJStRa2gMg==");
        try{
            Connection.Response res = Jsoup.connect("http://218.15.22.136:3008/login.aspx")
                    .data(params).timeout(30000).execute();
            Map<String, String> cookies = res.cookies();
        }catch (IOException ioe){
            return false;
        }
        return true;
    }*/
}
