package com.hawk.gshassist.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.hawk.gshassist.R;
import com.hawk.gshassist.ui.fragment.BaseFragment;
import com.hawk.gshassist.util.AppFragmentManager;
import com.hawk.gshassist.util.GSHNets;

import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseFragment.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";
    private AppFragmentManager mAppFragmentManager;
    private Map easCookies;
    private Map libCookies;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAppFragmentManager = new AppFragmentManager(this);

        navigationView.setCheckedItem(R.id.nav_easprofile);
        //默认先显示的fragment
        Bundle arguments = new Bundle();
        arguments.putInt("flag", 0);//切换到教务系统登录,flag=0表示登录的是教务系统
        mAppFragmentManager.switchToFragment(AppFragmentManager.EASLOGINFRAGMENT, arguments, false);

        progress=new ProgressDialog(this);
        progress.setCancelable(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            mAppFragmentManager.refreshFragment();
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int itemId = item.getItemId();
        boolean isEmptyCookie = false;

        if (itemId == R.id.nav_myblog) {
            //确认登录成功后是否移除了loginfragment
//            FragmentManager sfm=getSupportFragmentManager();
//            Log.i(TAG,"if contains loginfragment="+sfm.findFragmentByTag(AppFragmentManager.EASLOGINFRAGMENT));
//            Log.i(TAG,"if contains profilefragment="+sfm.findFragmentByTag(AppFragmentManager.EASPROFILEFRAGMENT));
            Bundle arguments = new Bundle();
            arguments.putString("webUrl", GSHNets.NET_MYBLOG);
            mAppFragmentManager.switchToFragment(AppFragmentManager.WEBVIEWFRAGMENT, arguments, false);
            isEmptyCookie = true;
        } else if (itemId == R.id.nav_easprofile || itemId == R.id.nav_courses || itemId == R.id.nav_scores) {
            //如果cookie有为空，切换到相应登录页面
            if (easCookies == null || easCookies.isEmpty()) {
                Bundle arguments = new Bundle();
                arguments.putInt("flag", 0);//切换到教务系统登录,flag=0表示登录的是教务系统
                mAppFragmentManager.switchToFragment(AppFragmentManager.EASLOGINFRAGMENT, arguments, false);
                isEmptyCookie = true;
            }
        } else if (itemId == R.id.nav_libprofile || itemId == R.id.nav_libcurborrow || itemId == R.id.nav_libborrowhis) {
            if (libCookies == null || libCookies.isEmpty()) {
                Bundle arguments = new Bundle();
                arguments.putInt("flag", 1);//切换到教务系统登录,flag=0表示登录的是教务系统
                mAppFragmentManager.switchToFragment(AppFragmentManager.LIBLOGINFRAGMENT, arguments, false);
                isEmptyCookie = true;
            }
        }

        //cookie不为空，切换到选择的页面
        if (!isEmptyCookie) {
            switch (itemId) {
                case R.id.nav_easprofile:
                    mAppFragmentManager.switchToFragment(AppFragmentManager.EASPROFILEFRAGMENT, null, false);
                    break;
                case R.id.nav_courses:
                    mAppFragmentManager.switchToFragment(AppFragmentManager.SEARCHCOURSESFRAGMENT, null, false);
                    break;
                case R.id.nav_scores:
                    mAppFragmentManager.switchToFragment(AppFragmentManager.SEARCHSCORESFRAGMENT, null, false);
                    break;
                case R.id.nav_libsearch:
                    mAppFragmentManager.switchToFragment(AppFragmentManager.SEARCHBOOKFRAGMENT, null, false);
                    break;
                case R.id.nav_libprofile:
                    mAppFragmentManager.switchToFragment(AppFragmentManager.LIBPROFILEFRAGMENT, null, false);
                    break;
                case R.id.nav_libcurborrow:
                    mAppFragmentManager.switchToFragment(AppFragmentManager.CURRENTBORROWFRAGMENT, null, false);
                    break;
                case R.id.nav_libborrowhis:
                    mAppFragmentManager.switchToFragment(AppFragmentManager.HISTORYBORROWFRAGMENT, null, false);
                    break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void switchToFragment(String fragmentTag, Bundle arguments, boolean removeFormer) {
        mAppFragmentManager.switchToFragment(fragmentTag, arguments, removeFormer);
    }

    @Override
    public void setCookies(Map cookies, int flag) {
        if (flag == 0) {
            easCookies = cookies;
        } else {
            libCookies = cookies;
        }
    }

    @Override
    public Map getCookies(int flag) {
        if (flag == 0) {
            return easCookies;
        } else {
            return libCookies;
        }
    }
    /**
     * 显示提示框
     * @param title
     * @param msg
     */
    @Override
    public void showProgress(String title,String msg){
        progress.setTitle(title);
        progress.setMessage(msg);
        progress.show();
    }

    /**
     * 隐藏提示框
     */
    @Override
    public void dismissProgress(){
        if(progress.isShowing()){
            progress.dismiss();
        }
    }

    @Override
    public void setTitleForFragment(String title) {
        setTitle(title);
    }
}
