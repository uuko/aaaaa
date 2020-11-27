package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class CustomRowsFragment extends RowsSupportFragment {

    private final int NUM_ROWS = 5;
    private final int NUM_COLS = 5;

    private ArrayObjectAdapter rowsAdapter;
    private CardPresenter cardPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(getRandomColor());
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadRows();
    }

    private void loadRows() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());
        cardPresenter = new CardPresenter();

        List<Movie> list = MovieList.setupMovies();

        int i;
        for (i = 0; i < NUM_ROWS; i++) {
            if (i != 0) {
                Collections.shuffle(list);
            }
            ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);
            for (int j = 0; j < NUM_COLS; j++) {
                listRowAdapter.add(list.get(j % 5));
            }
            HeaderItem header = new HeaderItem(i, MovieList.MOVIE_CATEGORY[i]);
            rowsAdapter.add(new ListRow(header, listRowAdapter));
        }

        setAdapter(rowsAdapter);
    }

    private int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
    public void refresh() {
        getView().setPadding(Utils.dpToPx(-24, getActivity()), Utils.dpToPx(128, getActivity()), Utils.dpToPx(300, getActivity()), 0);
    }
}