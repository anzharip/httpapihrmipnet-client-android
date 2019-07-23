package com.anzhari.hrmipnetmobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.anzhari.hrmipnetmobile.R;
import com.anzhari.hrmipnetmobile.adapter.ViewPagerAdapter;
import com.anzhari.hrmipnetmobile.fragment.AttachmentFragment;
import com.anzhari.hrmipnetmobile.fragment.PersonalDetailFragment;
import com.anzhari.hrmipnetmobile.util.UserSessionManager;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class PersonalDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabLayout;

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    private Unbinder unbinder;
    private UserSessionManager sessionManager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_detail);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sessionManager = new UserSessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout:
                sessionManager.logoutUser();
                nextAction();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter.addFragment(new PersonalDetailFragment(), "Personal Detail");
        adapter.addFragment(new AttachmentFragment(), "Attachments");
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
    }

    private void nextAction(){
        startActivity(new Intent(PersonalDetailActivity.this, LoginActivity.class));
        finish();
    }

}
