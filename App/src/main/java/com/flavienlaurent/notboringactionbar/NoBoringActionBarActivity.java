package com.flavienlaurent.notboringactionbar;

import android.app.Activity;
import android.app.ActionBar;
import android.content.res.Resources;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoBoringActionBarActivity extends Activity {

    private static final String TAG = "NoBoringActionBarActivity";
    private int mActionBarHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private ListView mListView;
    private ImageView mHeaderPicture;
    private ImageView mHeaderLogo;
    private View mHeader;
    private View mPlaceHolderView;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    public static float clamp(float f1, float f2, float f3) {
        return Math.max(Math.min(f1, f3), f2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSmoothInterpolator = new AccelerateDecelerateInterpolator();
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

        setContentView(R.layout.activity_noboringactionbar);

        mListView = (ListView) findViewById(R.id.listview);
        mHeader = findViewById(R.id.header);
        mHeaderPicture = (ImageView) findViewById(R.id.header_picture);
        mHeaderLogo = (ImageView) findViewById(R.id.header_logo);

        setupActionBar();
        setupListView();
    }

    private void setupListView() {
        ArrayList<String> FAKES = new ArrayList<String>();
        for (int i = 0; i < 1000; i++) {
            FAKES.add("entry " + i);
        }
        mPlaceHolderView = getLayoutInflater().inflate(R.layout.view_header_placeholder, mListView, false);
        mListView.addHeaderView(mPlaceHolderView);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FAKES));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int scrollY = getScrollY();
                //sticky actionbar
                mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                //header_logo --> actionbar icon
                float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
                interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));
                //actionbar title alpha
                getActionBarTitleView().setAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
            }
        });
    }

    private void interpolate(View view1, View view2, float interpolation) {
        RectF rectF1 = getOnScreenRect(view1);
        RectF rectF2 = getOnScreenRect(view2);

        Log.d(TAG, "rectF1: " + rectF1);
        Log.d(TAG, "rectF2: " + rectF2);

        float scaleX = 1.0F + interpolation * (rectF2.width() / rectF1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (rectF2.height() / rectF1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (rectF2.left + rectF2.right - rectF1.left - rectF1.right));
        float translationY = 0.5F * (interpolation * (rectF2.top + rectF2.bottom - rectF1.top - rectF1.bottom));

        view1.setTranslationX(translationX);
        view1.setTranslationY(translationY - mHeader.getTranslationY());
        view1.setScaleX(scaleX);
        view1.setScaleY(scaleY);
    }

    private RectF getOnScreenRect(View view) {
        RectF rectF = new RectF(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        for (ViewParent parent = view.getParent(); (parent != null) && ((parent instanceof ViewGroup)); parent = parent.getParent()) {
            ViewGroup viewGroup = (ViewGroup) parent;
            //rectF.offset(viewGroup.getLeft(), viewGroup.getTop());
        }
        return rectF;
    }

    public int getScrollY() {
        View c = mListView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mPlaceHolderView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    private void setupActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setIcon(R.drawable.ic_transparent);

        getActionBarTitleView().setAlpha(0f);
    }

    private ImageView getActionBarIconView() {
        return (ImageView) findViewById(android.R.id.home);
    }

    private TextView getActionBarTitleView() {
        int id = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        return (TextView) findViewById(id);
    }

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }
        final TypedValue tv = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
        mActionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        return mActionBarHeight;
    }
}