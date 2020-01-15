package com.example.weatherapp.ui.onboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.weatherapp.R;
import com.example.weatherapp.ui.base.BaseActivity;
import com.example.weatherapp.ui.main.MainActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;

import static com.example.weatherapp.R.string.*;

public class OnBoardActivity extends BaseActivity {
    @BindView(R.id.btn_next)
    Button btn_next;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R.id.tx_view)
    TextView textView;

    @Override
    protected int getViewLayout() {
        return R.layout.activity_on_board;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        tabLayout.setupWithViewPager(viewPager, true);
        viewPager();
        onNextClick();
        clickListener();

    }

    private void clickListener() {
        btn_next.setOnClickListener(v -> viewPager.setCurrentItem(viewPager.getCurrentItem() + 1));
    }

    private void viewPager() {
        OnBoardAdapter onBoardAdapter = new OnBoardAdapter(getResource());
        viewPager.setAdapter(onBoardAdapter);
    }

    private ArrayList<OnBoardEntity> getResource() {
        ArrayList<OnBoardEntity> list = new ArrayList<>();
        list.add(new OnBoardEntity(R.drawable.first_background, getString(first_string)));
        list.add(new OnBoardEntity(R.drawable.update_text, getString(second_string)));
        list.add(new OnBoardEntity(R.drawable.trash, getString(third_string)));
        list.add(new OnBoardEntity(R.drawable.thank_you, getString(four_string)));
        return list;
    }

    public void onNextClick() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }


            @Override
            public void onPageSelected(int position) {
                if (viewPager.getCurrentItem() == 3) {
                    btn_next.setText(Начать);
                    btn_next.setOnClickListener(v -> {
                        MainActivity.start(OnBoardActivity.this);
                        finish();
                    });
                } else {
                    btn_next.setText(Далее);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_onboard_skip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_skip:
                MainActivity.start(this);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, OnBoardActivity.class));
    }
}
