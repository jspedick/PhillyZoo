package com.example.phillyzoo;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    ViewPager viewPager=null;
    public static FragmentManager fragmentManager;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager= (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(2);

        fragmentManager = getSupportFragmentManager();

        final ActionBar actionBar=getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTabs(actionBar);
        viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
                Log.d("VIVZ","onPageScrolled "+i+" "+v+" "+i2);
            }

            @Override
            public void onPageSelected(int i) {
                actionBar.setSelectedNavigationItem(i);
                Log.d("VIVZ","onPageSelected "+i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if(i==ViewPager.SCROLL_STATE_IDLE)
                Log.d("VIVZ","onPageScrollStateChanged scroll state idle "+i);
                if(i==ViewPager.SCROLL_STATE_DRAGGING)
                    Log.d("VIVZ","onPageScrollStateChanged scroll state dragging "+i);
                if(i==ViewPager.SCROLL_STATE_SETTLING)
                    Log.d("VIVZ","onPageScrollStateChanged scroll state settling "+i);
            }
        });
    }
    
    private void addTabs(ActionBar actionBar)
    {
        ActionBar.Tab tab1=actionBar.newTab();
        tab1.setText("Info");
        tab1.setTabListener(this);

        ActionBar.Tab tab2=actionBar.newTab();
        tab2.setText("Map");
        tab2.setTabListener(this);

        ActionBar.Tab tab3=actionBar.newTab();
        tab3.setText("NewsFeed");
        tab3.setTabListener(this);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }
}

class MyAdapter extends FragmentStatePagerAdapter
{
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment=null;
        if(i==0)
        {
            fragment=new FragmentA();
        }
        if(i==1)
        {
        	
            fragment=new FragmentB();
        }
        if(i==2)
        {
            fragment=new FragmentC();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }
    
}