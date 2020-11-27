package com.example.myapplication;


import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


import androidx.fragment.app.Fragment;
import androidx.leanback.app.HeadersSupportFragment;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.HeaderItem;
import androidx.leanback.widget.ListRow;
import androidx.leanback.widget.OnChildSelectedListener;
import androidx.leanback.widget.VerticalGridView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class CustomHeadersFragment extends HeadersSupportFragment {

    private ArrayObjectAdapter adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        customSetBackground(R.color.colorAccent);

        setHeaderAdapter();
        VerticalGridView gridView = ((MainActivity) getActivity()).getVerticalGridView(this);
        gridView.setOnChildSelectedListener(new OnChildSelectedListener() {
            @Override
            public void onChildSelected(ViewGroup viewGroup, View view, int i, long l) {
                Object obj = ((ListRow) getAdapter().get(i)).getAdapter().get(0);
                getFragmentManager().beginTransaction().replace(R.id.rows_container, (androidx.fragment.app.Fragment) obj).commit();
                ((MainActivity) getActivity()).updateCurrentFragment((Fragment) obj);
            }
        });
//        gridView.setVerticalSpacing(400);
    }

    private void setHeaderAdapter() {
        adapter = new ArrayObjectAdapter();

        LinkedHashMap<Integer, Fragment> fragments = ((MainActivity) getActivity()).getFragments();

        int id = 0;
        for (int i = 0; i < fragments.size(); i++) {
            HeaderItem header = new HeaderItem(id, "Category " + i);
            ArrayObjectAdapter innerAdapter = new ArrayObjectAdapter();
            innerAdapter.add(fragments.get(i));
            adapter.add(id, new ListRow(header, innerAdapter));
            id++;
        }

        setAdapter(adapter);
    }



    /**
     * Since the original setBackgroundColor is private, we need to
     * access it via reflection
     *
     * @param colorResource The colour resource
     */
    private void customSetBackground(int colorResource) {
        try {
            Class clazz = HeadersSupportFragment.class;
            Method m = clazz.getDeclaredMethod("setBackgroundColor", Integer.TYPE);
            m.setAccessible(true);
            m.invoke(this, getResources().getColor(colorResource));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}