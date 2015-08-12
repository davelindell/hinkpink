package com.lindell.app.hinkpink;

/**
 * Created by lindell on 8/8/15.
 */

import java.sql.Connection;
import java.util.List;
import java.util.Vector;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.lindell.app.hinkpink.shared.models.HinkPinkUser;
//        import com.andy.R;
//        import com.andy.fragments.tabs.Tab1Fragment;
//        import com.andy.fragments.tabs.Tab2Fragment;
//        import com.andy.fragments.tabs.Tab3Fragment;


public class ConnectionPagerActivity extends FragmentActivity {
    /**
     * maintains the pager adapter
     */

    private HinkPinkUser user;

    private PagerAdapter mPagerAdapter;
    private ViewPager pager;
    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_connection_pager);
        user = new HinkPinkUser();
        user.setEmail(getIntent().getStringExtra("email"));
        user.setPassword(getIntent().getStringExtra("password"));
        //initialsie the pager
        this.initialisePaging();

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(pager);
        tabLayout.getTabAt(0).setText("Connections");
        tabLayout.getTabAt(1).setText("Requests");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    /**
     * Initialise the fragments to be paged
     */
    private void initialisePaging() {

        List<Fragment> fragments = new Vector<Fragment>();
        String email = user.getEmail();
        String password = user.getPassword();
        fragments.add(ConnectionListFragment.newInstance(user.getEmail(), user.getPassword()));
        fragments.add(IncomingConnectionFragment.newInstance(user.getEmail(), user.getPassword()));
        this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);

        pager = (ViewPager) super.findViewById(R.id.viewpager);
        pager.setAdapter(this.mPagerAdapter);
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        final int PAGE_COUNT = 2;
        private String tabTitles[] = new String[] { "Connections", "Requests"};
        private List<Fragment> fragments;

        /**
         * @param fm
         * @param fragments
         */
        public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        /* (non-Javadoc)
         * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
         */
        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getCount()
         */
        @Override
        public int getCount() {
            return this.fragments.size();
        }

    }
}