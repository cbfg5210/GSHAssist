package com.hawk.gshassist.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.hawk.gshassist.R;
import com.hawk.gshassist.ui.MainActivity;
import com.hawk.gshassist.ui.fragment.BaseFragment;
import com.hawk.gshassist.ui.fragment.CurrentBorrowFragment;
import com.hawk.gshassist.ui.fragment.EASProfileFragment;
import com.hawk.gshassist.ui.fragment.HistoryBorrowFragment;
import com.hawk.gshassist.ui.fragment.LibProfileFragment;
import com.hawk.gshassist.ui.fragment.LoginFragment;
import com.hawk.gshassist.ui.fragment.SearchBookFragment;
import com.hawk.gshassist.ui.fragment.SearchCoursesFragment;
import com.hawk.gshassist.ui.fragment.SearchScoresFragment;
import com.hawk.gshassist.ui.fragment.WebViewFragment;

/**
 * 在此写用途
 * Created by hawk on 2016/3/15.
 */
public class AppFragmentManager {
    private static final String TAG = "AppFragmentManager";

    public static final String EASLOGINFRAGMENT = "EASLOGINFRAGMENT";
    public static final String EASPROFILEFRAGMENT = "EASPROFILEFRAGMENT";
    public static final String SEARCHCOURSESFRAGMENT = "SEARCHCOURSESFRAGMENT";
    public static final String SEARCHSCORESFRAGMENT = "SEARCHSCORESFRAGMENT";
    public static final String LIBLOGINFRAGMENT = "LIBLOGINFRAGMENT";
    public static final String LIBPROFILEFRAGMENT = "LIBPROFILEFRAGMENT";
    public static final String CURRENTBORROWFRAGMENT = "CURRENTBORROWFRAGMENT";
    public static final String HISTORYBORROWFRAGMENT = "HISTORYBORROWFRAGMENT";
    public static final String SEARCHBOOKFRAGMENT = "SEARCHBOOKFRAGMENT";
    public static final String WEBVIEWFRAGMENT = "WEBVIEWFRAGMENT";

    private FragmentManager mFragmentManager;
    private String currentFragmentTag = "";

    public AppFragmentManager(MainActivity mainActivity) {
        mFragmentManager = mainActivity.getSupportFragmentManager();
    }

//    public void setCurrentFragmentTag(String tag) {
//        currentFragmentTag = tag;
//    }
//
//    public String getCurrentFragmentTag() {
//        return currentFragmentTag;
//    }

    public void removeFragment(String tag) {
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (null == fragment) return;
        mFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
    }

    public void switchToFragment(String tag, Bundle arguments,boolean removeFormer) {
        if (tag.equals(currentFragmentTag)) {
            return;
        }
        Fragment fragment = mFragmentManager.findFragmentByTag(tag);
        if (null == fragment) {
            if (tag.equals(EASLOGINFRAGMENT)) {
                fragment = new LoginFragment();
            } else if (tag.equals(EASPROFILEFRAGMENT)) {
                fragment = new EASProfileFragment();
            } else if (tag.equals(SEARCHCOURSESFRAGMENT)) {
                fragment = new SearchCoursesFragment();
            } else if (tag.equals(SEARCHSCORESFRAGMENT)) {
                fragment = new SearchScoresFragment();
            } else if (tag.equals(LIBLOGINFRAGMENT)) {
                fragment = new LoginFragment();
            } else if (tag.equals(LIBPROFILEFRAGMENT)) {
                fragment = new LibProfileFragment();
            } else if (tag.equals(CURRENTBORROWFRAGMENT)) {
                fragment = new CurrentBorrowFragment();
            } else if (tag.equals(HISTORYBORROWFRAGMENT)) {
                fragment = new HistoryBorrowFragment();
            }else if (tag.equals(SEARCHBOOKFRAGMENT)) {
                fragment = new SearchBookFragment();
            }else if (tag.equals(WEBVIEWFRAGMENT)) {
                fragment = new WebViewFragment();
            }

            fragment.setArguments(arguments);
        }
        if (fragment.isAdded()) {
            Fragment currentFragment = mFragmentManager.findFragmentByTag(currentFragmentTag);
            FragmentTransaction ftrans = mFragmentManager.beginTransaction();
            ftrans.hide(currentFragment).replace(R.id.abman_fragment, fragment).show(fragment).commitAllowingStateLoss();
        } else {
            if (TextUtils.isEmpty(currentFragmentTag)) {
                mFragmentManager.beginTransaction().add(R.id.abman_fragment, fragment, tag).commitAllowingStateLoss();
            } else {
                Fragment currentFragment = mFragmentManager.findFragmentByTag(currentFragmentTag);
                mFragmentManager.beginTransaction().add(R.id.abman_fragment, fragment, tag).hide(currentFragment).show(fragment).commitAllowingStateLoss();
            }
        }
        if(removeFormer){
            removeFragment(currentFragmentTag);
        }
        currentFragmentTag = tag;
    }

    public void refreshFragment(){
        if(TextUtils.isEmpty(currentFragmentTag))return;
        BaseFragment fragment = (BaseFragment) mFragmentManager.findFragmentByTag(currentFragmentTag);
        fragment.refreshFragment();
    }
}
