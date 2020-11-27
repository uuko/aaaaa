package com.example.myapplication;


import android.content.Intent;
import android.os.Bundle;
import androidx.leanback.widget.ArrayObjectAdapter;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.ListRowPresenter;


/**
 * Created by Sebastiano Gottardo on 03/05/15.
 */
public class MoreSamplesFragment extends RowsSupportFragment {

    private ArrayObjectAdapter rowsAdapter;

    private static final int HEADERS_FRAGMENT_SCALE_SIZE = 300;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         v = super.onCreateView(inflater, container, savedInstanceState);

        int marginOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, HEADERS_FRAGMENT_SCALE_SIZE, getResources().getDisplayMetrics());
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        params.rightMargin -= marginOffset;
        v.setLayoutParams(params);

        return v;

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadRows();
        setCustomPadding();
//        setOnItemViewClickedListener(new OnItemViewClickedListener() {
//            @Override
//            public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
//                if (((String) item).indexOf(getString(R.string.app_name)) >= 0) {
//                    Intent intent = new Intent(getActivity(), VerticalGridActivity.class);
//                    getActivity().startActivity(intent);
//                } else if (((String) item).indexOf(getString(R.string.error_fragment)) >= 0) {
//                    Intent intent = new Intent(getActivity(), BrowseErrorActivity.class);
//                    getActivity().startActivity(intent);
//                } else if (((String) item)
//                        .indexOf(getString(R.string.guidedstep_first_title)) >= 0) {
//                    Intent intent = new Intent(getActivity(), GuidedStepActivity.class);
//                    startActivity(intent);
//                } else {
//                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
//                    getActivity().startActivity(intent);
//                }
//            }
//        });
    }

    private void setCustomPadding() {
        getView().setPadding(Utils.convertDpToPixel(getActivity(), -24), Utils.convertDpToPixel(getActivity(), 128), Utils.convertDpToPixel(getActivity(), 48), 0);
    }

    private void loadRows() {
        rowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        HeaderItem gridHeader = new HeaderItem(1, getString(R.string.app_name));
        GridItemPresenter gridPresenter = new GridItemPresenter(this);

        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(gridPresenter);
        gridRowAdapter.add(getString(R.string.app_name));
        gridRowAdapter.add(getString(R.string.app_name));
        gridRowAdapter.add(getString(R.string.app_name));
        gridRowAdapter.add(getString(R.string.app_name));
        gridRowAdapter.add(getString(R.string.app_name));

        rowsAdapter.add(new ListRow(gridHeader, gridRowAdapter));

        setAdapter(rowsAdapter);
    }
}

