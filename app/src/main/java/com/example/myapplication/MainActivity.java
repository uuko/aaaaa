package com.example.myapplication;




import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.leanback.app.RowsSupportFragment;
import androidx.leanback.widget.HorizontalGridView;
import androidx.leanback.widget.VerticalGridView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;

public class MainActivity extends FragmentActivity {
    private CustomFrameLayout customFrameLayout;
    private static final float NAVIGATION_DRAWER_SCALE_FACTOR = 0.9f;
    private boolean navigationDrawerOpen;
    private CustomHeadersFragment headersFragment;
    private CustomRowsFragment rowsFragment;
    private Fragment currentFragment;
    private final int CATEGORIES_NUMBER = 5;
    private LinkedHashMap<Integer, Fragment> fragments;
    private MoreSamplesFragment moreSamplesFragment;
    private int keyCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        headersFragment = new CustomHeadersFragment();
        rowsFragment = new CustomRowsFragment();
        FragmentManager fragmentManager =  getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction
                .replace(R.id.header_container, headersFragment, "CustomHeadersFragment")
                .replace(R.id.rows_container, rowsFragment, "CustomRowsFragment");
        View v=findViewById(R.id.rows_container);
        transaction.commit();
        currentFragment = rowsFragment;
        fragments = new LinkedHashMap<Integer, Fragment>();
        moreSamplesFragment = new MoreSamplesFragment();
        fragments.put(0,rowsFragment);
        fragments.put(1,moreSamplesFragment);

        customFrameLayout = (CustomFrameLayout) ((ViewGroup) this.findViewById(android.R.id.content)).getChildAt(0);
        setupCustomFrameLayout();

    }

    public VerticalGridView getVerticalGridView(Fragment fragment) {
        try {
//androidx.leanback.app
            Class baseRowFragmentClass = getClassLoader().loadClass("androidx/leanback/app/BaseRowSupportFragment");
            Method getVerticalGridViewMethod = baseRowFragmentClass.getDeclaredMethod("getVerticalGridView", null);
            getVerticalGridViewMethod.setAccessible(true);
            VerticalGridView gridView = (VerticalGridView) getVerticalGridViewMethod.invoke(fragment, null);

            return gridView;

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void updateCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
    }

    public LinkedHashMap<Integer, Fragment> getFragments() {
        return fragments;
    }
    private void setupCustomFrameLayout() {

        customFrameLayout.setOnChildFocusListener(new CustomFrameLayout.OnChildFocusListener() {
            @Override
            public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
                if (headersFragment.getView() != null && headersFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
                if (rowsFragment.getView() != null && rowsFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
                return false;
            }

            @Override
            public void onRequestChildFocus(View child, View focused) {
                int childId = child.getId();
                if (childId == R.id.rows_container) {
                    toggleHeadersFragment(false);
                } else if (childId == R.id.header_container) {
                    toggleHeadersFragment(true);
                }
            }
        });

        customFrameLayout.setOnFocusSearchListener(new CustomFrameLayout.OnFocusSearchListener() {
            @Override
            public View onFocusSearch(View focused, int direction) {
                if (direction == View.FOCUS_LEFT) {
                    if (isVerticalScrolling() || navigationDrawerOpen ) {
                        return focused;
                    }
                    return getVerticalGridView(headersFragment);
                } else if (direction == View.FOCUS_RIGHT) {
                    if (isVerticalScrolling() || !navigationDrawerOpen) {
                        return focused;
                    }
                    return getVerticalGridView(rowsFragment);}
                else {
                    return null;
                }
            }
        });
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public synchronized void toggleHeadersFragment(final boolean doOpen) {

        boolean condition = (doOpen != isNavigationDrawerOpen());
        if (condition) {

            final View headersContainer = (View) headersFragment.getView().getParent();
            final View rowsContainer = (View) currentFragment.getView().getParent();
            Log.d("vvvvvvvvvvvvvvvvvv", "rowsContainer: "+rowsContainer.getMeasuredWidth());
            Log.d("vvvvvvvvvvvvvvvvvv", "headersContainer: "+headersContainer.getMeasuredWidth());
            final float delta = headersContainer.getWidth() * NAVIGATION_DRAWER_SCALE_FACTOR;

            // get current margin (a previous animation might have been interrupted)
            final int currentHeadersMargin = (((ViewGroup.MarginLayoutParams) headersContainer.getLayoutParams()).leftMargin);
            final int currentRowsMargin = (((ViewGroup.MarginLayoutParams) rowsContainer.getLayoutParams()).leftMargin);

            // calculate destination
            final int headersDestination = (doOpen ? 0 : (int) (0 - delta));
            final int rowsDestination = (doOpen ? (Utils.convertDpToPixel(this, 300)) : (int) (Utils.convertDpToPixel(this, 300) - delta));

            // calculate the delta (destination - current)
            final int headersDelta = headersDestination - currentHeadersMargin;
            final int rowsDelta = rowsDestination - currentRowsMargin;

            Animation animation = new Animation() {
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    ViewGroup.MarginLayoutParams headersParams = (ViewGroup.MarginLayoutParams) headersContainer.getLayoutParams();
                    headersParams.leftMargin = (int) (currentHeadersMargin + headersDelta * interpolatedTime);
                    headersContainer.setLayoutParams(headersParams);

                    ViewGroup.MarginLayoutParams rowsParams = (ViewGroup.MarginLayoutParams) rowsContainer.getLayoutParams();
                    rowsParams.leftMargin = (int) (currentRowsMargin + rowsDelta * interpolatedTime);
                    rowsContainer.setLayoutParams(rowsParams);
                }
            };

            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    navigationDrawerOpen = doOpen;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!doOpen && currentFragment instanceof CustomRowsFragment) {
                        rowsFragment.refresh();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}

            });

            animation.setDuration(200);
            ((View) rowsContainer.getParent()).startAnimation(animation);
        }
    }
    public synchronized boolean isNavigationDrawerOpen() {

        return navigationDrawerOpen;
    }
    private boolean isVerticalScrolling() {
        try {
            // don't run transition
            return getVerticalGridView(headersFragment).getScrollState()
                    != HorizontalGridView.SCROLL_STATE_IDLE
                    || getVerticalGridView(rowsFragment).getScrollState()
                    != HorizontalGridView.SCROLL_STATE_IDLE;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==23){
            this.keyCode=keyCode;
        }
        return super.onKeyDown(keyCode, event);
    }
}