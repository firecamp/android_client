package firecamp.android_client;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity implements TabListener {

    static final int TAB_COUNT = 4;
    static final Class<?>[] TAB_FRAGMENTS = new Class<?>[TAB_COUNT];
    private static final int TAB_TITLE_IDS[] = new int[TAB_COUNT];

    static {

        TAB_FRAGMENTS[0] = EmptyFragment.class;
        TAB_TITLE_IDS[0] = R.string.tab_map;

        TAB_FRAGMENTS[1] = EmptyFragment.class;
        TAB_TITLE_IDS[1] = R.string.tab_events;

        TAB_FRAGMENTS[2] = EmptyFragment.class;
        TAB_TITLE_IDS[2] = R.string.tab_people;

        TAB_FRAGMENTS[3] = EmptyFragment.class;
        TAB_TITLE_IDS[3] = R.string.tab_profile;

    }

    private ViewPager viewPager;

    private void addTab(ActionBar actionBar, int titleId) {
        actionBar.addTab(actionBar.newTab()
                .setText(getResources().getString(titleId))
                .setTabListener(this));
    }

    private void addTabs(ActionBar actionBar) {
        for (int position = 0; position < TAB_COUNT; ++position) {
            addTab(actionBar, TAB_TITLE_IDS[position]);
        }
    }

    private void initialize() {
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        addTabs(actionBar);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initialize();
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

}

class ViewPagerAdapter extends FragmentStatePagerAdapter {

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) MainActivity.TAB_FRAGMENTS[position]
                    .newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return MainActivity.TAB_COUNT;
    }

}
