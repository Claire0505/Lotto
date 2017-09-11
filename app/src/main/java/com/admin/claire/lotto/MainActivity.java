package com.admin.claire.lotto;


import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.admin.claire.lotto.adapter.MyPagerAdapter;
import com.admin.claire.lotto.adapter.MyRecyclerViewAdapter;
import com.admin.claire.lotto.fragment.BigLottoFragment;
import com.admin.claire.lotto.fragment.HistoryFragment;
import com.admin.claire.lotto.fragment.Lotto539Fragment;
import com.admin.claire.lotto.fragment.LottoFragment;
import com.admin.claire.lotto.fragment.StarLottoFragment;
import com.admin.claire.lotto.fragment.SupperLottoFragment;

/**
 * 搭配TabLayout 和 ViewPager 切換頁面
 * 使用自定義的MyPagerAdapter extends FragmentPagerAdapter，來切換各個fragment
 *
 * Fragment Navigation Drawer
 * 1.Create a menu/drawer_view.xml file 建立<group></group>放要顯示的item項目
 * create sub headers too and group elements together 創建子標題
 *
 * 2.Create a new layout file toolbar.xml with the following code
 * 要使用工具欄作為ActionBar，需要禁用默認的ActionBar。這可以通過在styles.xml設置應用程序主題來實現。Theme.AppCompat.Light.NoActionBar
 * 3.將activity_main.xml 佈局改成使用DrawerLayout,並添加<include>把toolbar.xml加入</include>
 *   再加上左側的導航抽屜 <android.support.design.widget.NavigationView>
 * 4.NavigationView還接受一個自定義屬性，引用一個提供佈局標題的佈局 layout/nav_header.xml
 *  headerLayout自定義屬性在NavigationView的activity_main.xml中引用此 app:headerLayout="@layout/nav_header"
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private MyRecyclerViewAdapter mAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigationDrawer();
        myTabViewPager();

    }

    private void initNavigationDrawer() {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = (DrawerLayout)findViewById(R.id.draw_layout);
        nvDrawer = (NavigationView)findViewById(R.id.nvView);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);

    }

    private ActionBarDrawerToggle setupDrawerToggle(){
        return new ActionBarDrawerToggle(this,mDrawer,toolbar,
                R.string.drawer_open,R.string.drawer_close);
    }

    // Setup drawer view
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                //左側的導航抽屜切換不同Fragment
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem){
        //左側的導航抽屜切換不同Fragment
        switch (menuItem.getItemId()){
            case R.id.nav_first_fragment:
                openFragment(new BigLottoFragment(),"大福彩");
                break;
            case R.id.nav_Second_fragment:
                openFragment(new Lotto539Fragment(),"今彩539");
                break;
            case R.id.nav_Third_fragment:
                openFragment(new StarLottoFragment(),"3星彩,4星彩");
                break;

            case R.id.nav_taiwan_lottery:
                String url = "http://www.taiwanlottery.com.tw/index_new.aspx";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();


    }

    private void openFragment(final Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
        getSupportActionBar().setTitle(title);

    }

    private void myTabViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        MyPagerAdapter pagerAdapter =
                new MyPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;


        }
        return super.onOptionsItemSelected(item);
    }
}
