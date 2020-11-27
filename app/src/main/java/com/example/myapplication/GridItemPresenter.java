package com.example.myapplication;


import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.leanback.widget.Presenter;

public class GridItemPresenter extends Presenter {
    private static int GRID_ITEM_WIDTH = 300;
    private static int GRID_ITEM_HEIGHT = 200;
// 除以9
    private Fragment mainFragment;

    public GridItemPresenter(Fragment fragment) {
        this.mainFragment = fragment;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        TextView view = new TextView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setBackgroundColor(mainFragment.getResources().getColor(R.color.colorAccent));
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT);
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
        marginLayoutParams.setMargins(20,0,20,200);

        view.setLayoutParams(marginLayoutParams);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        ((TextView) viewHolder.view).setText((String) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {
    }
}

