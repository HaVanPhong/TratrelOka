package com.example.traveloka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.traveloka.Volley.Account;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView btnNavi;
    ViewPager viewPager;
    Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar= getSupportActionBar();
        actionBar.hide();
        Intent intent= getIntent();
        account= intent.getParcelableExtra("account");
        setAccount(account);
        Log.d("tx", "id nhận: "+ account.getId());
        btnNavi = findViewById(R.id.btnNavigationView);
        viewPager = findViewById(R.id.viewPager);

        setupViewPager();

        btnNavi.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_trangChu:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.action_xe:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.action_lich:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }

    private void setupViewPager() {
        ViewPgAdapter adapter = new ViewPgAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        btnNavi.getMenu().findItem(R.id.action_trangChu).setChecked(true);
                        break;
                    case 1:
                        btnNavi.getMenu().findItem(R.id.action_xe).setChecked(true);
                        break;
                    case 2:
                        btnNavi.getMenu().findItem(R.id.action_lich).setChecked(true);
                        break;

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}