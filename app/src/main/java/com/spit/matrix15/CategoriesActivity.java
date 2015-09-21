/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.spit.matrix15;

import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class CategoriesActivity extends AppCompatActivity implements RecyclerViewAdapter.OnItemClickListener, AboutUsFragment.OnFragmentInteractionListener, CategoriesFragment.OnFragmentInteractionListener, DevelopersFragment.OnFragmentInteractionListener {

    public static final String AVATAR_URL = "http://lorempixel.com/200/200/people/1/";
    public static final String[] Items = {"Mega Events","Technical","Sports","Literary","Coding","Fun Events"};
    public static final int[] Imgs = {R.drawable.pic1,R.drawable.pic2,R.drawable.pic3,
                            R.drawable.pic4,R.drawable.pic5,R.drawable.pic6};
    private static List<ViewModel> funItems = new ArrayList<>();
    private static List<ViewModel> codingItems = new ArrayList<>();
    private static List<ViewModel> preItems = new ArrayList<>();
    private static List<ViewModel> litItems = new ArrayList<>();

    static {

    }



    private DrawerLayout drawerLayout;
    private View content;
    private ViewPager mPager;
    private YourPagerAdapter mAdapter;
    private TabLayout mTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initRecyclerView();
        //initFab();
        initToolbar();
        setupDrawerLayout();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_container, new CategoriesFragment());
        ft.commit();

        if (funItems.isEmpty() || preItems.isEmpty() || litItems.isEmpty() || codingItems.isEmpty()) {
            List<Event> funEvents = Event.find(Event.class, "event_category = ?", "Fun and Games");
            for (Event event : funEvents) {
                funItems.add(new ViewModel(event.eventName, "http://matrixthefest.org/app_posters/" + event.eventPoster));
            }

            List<Event> codingEvents = Event.find(Event.class, "event_category = ?", "Coding");
            for (Event event : codingEvents) {
                codingItems.add(new ViewModel(event.eventName, "http://matrixthefest.org/app_posters/" + event.eventPoster));
            }
            List<Event> preEvents = Event.find(Event.class, "event_category = ?", "Pre-Fest");
            for (Event event : preEvents) {
                preItems.add(new ViewModel(event.eventName, "http://matrixthefest.org/app_posters/" + event.eventPoster));
            }
            List<Event> litEvents = Event.find(Event.class, "event_category = ?", "Literary");
            for (Event event : litEvents) {
                litItems.add(new ViewModel(event.eventName, "http://matrixthefest.org/app_posters/" + event.eventPoster));
            }
        }
//
//        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        mAdapter = new YourPagerAdapter(getSupportFragmentManager());
//        mPager = (ViewPager) findViewById(R.id.view_pager);
//        mPager.setAdapter(mAdapter);
//        //Notice how the Tab Layout links with the Pager Adapter
//        mTabLayout.setTabsFromPagerAdapter(mAdapter);
//
//        //Notice how The Tab Layout adn View Pager object are linked
//        mTabLayout.setupWithViewPager(mPager);
//        mPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
//
//
//
//        content = findViewById(R.id.content);
//
//        final ImageView avatar = (ImageView) findViewById(R.id.avatar);
//        Picasso.with(this).load(AVATAR_URL).transform(new CircleTransform()).into(avatar);
    }

    /*private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(items);

        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }*/

/*    private void initFab() {
        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Snackbar.make(content, "FAB Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
    }*/

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed (){
        
    }

    private void setupDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);
        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                int menuItemId = menuItem.getItemId();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                switch (menuItemId) {
                    case R.id.drawer_favourite:
                        ft.replace(R.id.fragment_container, new AboutUsFragment());
                        ft.commit();
                        break;
                    case  R.id.drawer_home:
                        ft.replace(R.id.fragment_container, new CategoriesFragment());
                        ft.commit();
                        break;
                    case R.id.drawer_downloaded:
                        ft.replace(R.id.fragment_container, new DevelopersFragment());
                        ft.commit();
                        break;

                }
//                Snackbar.make(content, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override public void onItemClick(View view, ViewModel viewModel) {
        DetailActivity.navigate(this, view.findViewById(R.id.image), viewModel);
    }

    public void onFragmentInteraction(Uri uri) {

    }

    public static class MyFragment extends Fragment {
        public static final java.lang.String ARG_PAGE = "arg_page";


        public MyFragment() {

        }

        public static MyFragment newInstance(int pageNumber) {
            MyFragment myFragment = new MyFragment();
            Bundle arguments = new Bundle();
            arguments.putInt(ARG_PAGE, pageNumber + 1);
            myFragment.setArguments(arguments);
            return myFragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            Bundle arguments = getArguments();
            int pageNumber = arguments.getInt(ARG_PAGE);
            RecyclerView recyclerView = new RecyclerView(getActivity());
            RecyclerViewAdapter adapter;
            switch (pageNumber) {
                case 1:
                    adapter = new RecyclerViewAdapter(codingItems);
                    break;
                case 2:
                    adapter = new RecyclerViewAdapter(preItems);
                    break;
                case 3:
                    adapter = new RecyclerViewAdapter(litItems);
                    break;
                case 4:
                    adapter = new RecyclerViewAdapter(funItems);
                    break;
                default:
                    adapter = new RecyclerViewAdapter(funItems);
            }
            //recyclerView.setAdapter(new YourRecyclerAdapter(getActivity()));

            adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, ViewModel viewModel) {
                    DetailActivity.navigate((AppCompatActivity) getActivity(), view.findViewById(R.id.image), viewModel);
                }
            });
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            return recyclerView;
        }
    }
}

class YourPagerAdapter extends FragmentStatePagerAdapter {

    public YourPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        CategoriesActivity.MyFragment myFragment = CategoriesActivity.MyFragment.newInstance(position);
        return myFragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Coding";
            case 1:
                return "Pre-Fest";
            case 2:
                return "Literary";
            case 3:
                return "Fun and Games";
            default:
                return "None";
        }
    }

}

