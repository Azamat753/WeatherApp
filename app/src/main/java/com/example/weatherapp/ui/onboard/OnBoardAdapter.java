package com.example.weatherapp.ui.onboard;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.weatherapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnBoardAdapter extends PagerAdapter {
    private ArrayList<OnBoardEntity> list;
    @BindView(R.id.image_view)
    ImageView imageView;
    @BindView(R.id.tx_view2)
    TextView textView;

    public OnBoardAdapter(ArrayList<OnBoardEntity> board) {
        this.list = board;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        @SuppressLint("InflateParams") View v = LayoutInflater.from(container.getContext()).inflate(R.layout.board_view, null);
        ButterKnife.bind(this, v);
        initView(v, container, position);
        return v;
    }

    private void initView(View view, ViewGroup viewGroup, int position) {
        imageView.setImageDrawable(viewGroup.getContext().getResources().getDrawable(list.get(position).getImageView()));
        viewGroup.addView(view);
        textView.setText(list.get(position).getTitle());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == ((View) object);

    }
}
