package jon.com.securitometer;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {
    private Device device;
    private PackageManager pm;
    private ViewPager viewPager;
    private ActionBar actionBar;
    private PagerAdapter pagerAdapter;

    private String[] tabs = { "Overview", "Apps", "Operating System" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pm = getPackageManager();

        device = new Device();

        viewPager = (ViewPager)findViewById(R.id.pager);
        actionBar = getSupportActionBar();
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(pagerAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                    .setTabListener(this));
        }

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // on changing the page
                // make respected tab selected
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        List<PackageInfo> installedApplications = pm.getInstalledPackages(PackageManager.GET_PERMISSIONS);
        device.apps = App.scanApps(installedApplications, this.getApplicationContext());
        double sum = 0.0;
        for(App a : device.apps) {
            sum += a.getScore();
        }
        device.setAppScore(sum / device.apps.size());

        device.os = OS.scanOS(android.os.Build.VERSION.RELEASE, getApplicationContext());
        device.setOsScore(device.os.getScore());

        Securitometer securitometer = (Securitometer) getApplicationContext();
        securitometer.setDevice(device);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // on tab selected
        // show respected fragment view
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public static class PagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        private AppFragment appFragment;
        private SystemFragment sysFragment;
        private OSFragment osFragment;

        public PagerAdapter(FragmentManager fm){ super(fm); }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    if(sysFragment == null) {
                        sysFragment = SystemFragment.newInstance();
                    }
                    return sysFragment;
                case 1:
                    if(appFragment == null) {
                        appFragment = AppFragment.newInstance();
                    }
                    return appFragment;
                case 2:
                    if(osFragment == null){
                        osFragment = OSFragment.newInstance();
                    }
                    return osFragment;
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0:
                    return "Overall";
                case 1:
                    return "Applications";
                case 2:
                    return "Operating System";
            }
            return null;
        }
    }
}
