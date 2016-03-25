package com.hawk.gshassist.util;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * 在此写用途
 * Created by hawk on 2016/3/21.
 */
public class DialogUtil {
    public static ProgressDialog getProgressDialog(Context context, String msg){
        ProgressDialog instance=new ProgressDialog(context);
        instance.setMessage(msg);
        instance.setCanceledOnTouchOutside(false);
        return instance;
    }
}
