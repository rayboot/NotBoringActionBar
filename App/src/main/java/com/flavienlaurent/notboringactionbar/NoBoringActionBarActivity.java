package com.flavienlaurent.notboringactionbar;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.nineoldandroids.view.ViewHelper;
import java.util.ArrayList;

public class NoBoringActionBarActivity extends Activity
{

    private static final String TAG = "NoBoringActionBarActivity";
    @InjectView(R.id.ivUserLogo) ImageView mIvUserLogo;
    @InjectView(R.id.ivUserSuper) ImageView mIvUserSuper;
    @InjectView(R.id.tvUserName) TextView mTvUserName;
    @InjectView(R.id.tvDesc) TextView mTvDesc;
    @InjectView(R.id.tvFansAttentions) TextView mTvFansAttentions;
    @InjectView(R.id.ivUserSex) ImageView mIvUserSex;
    @InjectView(R.id.hsvYear) HorizontalScrollView mHsvYear;
    @InjectView(R.id.button) Button mButton;
    @InjectView(R.id.button2) Button mButton2;
    @InjectView(R.id.tvActionBarTitle) TextView mTvActionBarTitle;
    @InjectView(R.id.actionbar) RelativeLayout mActionbar;
    @InjectView(R.id.listview) ListView mListView;
    @InjectView(R.id.header_picture) ImageView mHeaderPicture;
    @InjectView(R.id.rlUserLogo) RelativeLayout mRlHeaderLogoView;
    @InjectView(R.id.header) View mHeader;
    private View mPlaceHolderView;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;

    float distance = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mHeaderHeight =
                getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation =
                -mHeaderHeight + getActionBarHeight() + getScrollYearHeight();

        setContentView(R.layout.activity_noboringactionbar);
        ButterKnife.inject(this);

        setupListView();
    }

    private void setupListView()
    {
        ArrayList<String> FAKES = new ArrayList<String>();
        for (int i = 0; i < 40; i++)
        {
            FAKES.add("entry " + i);
        }
        mPlaceHolderView =
                getLayoutInflater().inflate(R.layout.view_header_placeholder,
                        mListView, false);
        mListView.addHeaderView(mPlaceHolderView);
        mListView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, FAKES));
        mListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount)
            {
                int scrollY = getScrollY();
                mHeader.setTranslationY(
                        Math.max(-scrollY, mMinHeaderTranslation));
                myInterpolate(mTvUserName, mTvActionBarTitle);
                setTitleAlpha();
            }
        });
    }

    private void setTitleAlpha()
    {
        float alpha =
                Math.max(0.0f, (distance - Math.abs(mHeader.getTranslationY())) / distance);
        ViewHelper.setAlpha(mRlHeaderLogoView, alpha);
        ViewHelper.setAlpha(mIvUserSex, alpha);
        ViewHelper.setAlpha(mTvDesc, alpha);
        ViewHelper.setAlpha(mTvFansAttentions, alpha);
    }

    public static float clamp(float value, float max, float min)
    {
        return Math.max(Math.min(value, min), max);
    }

    private void myInterpolate(View view1, View view2)
    {
        if (distance < 1.0f)
        {
            distance = view1.getTop() - view2.getTop();
        }
        view1.setVisibility(
                Math.abs(mHeader.getTranslationY()) > distance ? View.INVISIBLE
                        : View.VISIBLE);
        view2.setVisibility(
                Math.abs(mHeader.getTranslationY()) > distance ? View.VISIBLE
                        : View.INVISIBLE);
    }

    public int getScrollY()
    {
        View c = mListView.getChildAt(0);
        if (c == null)
        {
            return 0;
        }

        int firstVisiblePosition = mListView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1)
        {
            headerHeight = mPlaceHolderView.getHeight();
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    public int getActionBarHeight()
    {
        return getResources().getDimensionPixelSize(
                R.dimen.mine_actionbar_height);
    }

    public int getScrollYearHeight()
    {
        return getResources().getDimensionPixelSize(
                R.dimen.mine_scroll_year_height);
    }
}
