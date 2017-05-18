package com.example.call_the_roll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.promeg.pinyinhelper.Pinyin;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    private PagerAdapter mAdapter;
    private List<View> mViews = new ArrayList<View>();

    private LinearLayout mTabCallup;
    private LinearLayout mTabSignup;
    private LinearLayout mTabMine;

    private ImageButton mCallupImg;
    private ImageButton mSignupImg;
    private ImageButton mMineImg;
    private ImageButton mAddImg;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initView();

        initEvents();

        //String str = "界面侧边ABc字母检索";
        //for(char c:str.toCharArray()){
            //Log.d("MainActivity", Pinyin.toPinyin(c));
        //}

    }

    private void initView()
    {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        // tabs
        mTabCallup = (LinearLayout) findViewById(R.id.id_tab_callup);
        mTabSignup = (LinearLayout) findViewById(R.id.id_tab_signup);
        mTabMine = (LinearLayout) findViewById(R.id.id_tab_mine);

        // ImageButton
        mCallupImg = (ImageButton) findViewById(R.id.id_tab_callup_img);
        mSignupImg = (ImageButton) findViewById(R.id.id_tab_signup_img);
        mMineImg = (ImageButton) findViewById(R.id.id_tab_mine_img);
        mAddImg = (ImageButton) findViewById(R.id.Add_item);

        LayoutInflater mInflater = LayoutInflater.from(this);
        View tab01 = mInflater.inflate(R.layout.tab01, null);
        View tab02 = mInflater.inflate(R.layout.tab02, null);
        View tab03 = mInflater.inflate(R.layout.tab03, null);
        mViews.add(tab01);
        mViews.add(tab02);
        mViews.add(tab03);

        mAdapter = new PagerAdapter()
        {

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object)
            {
                container.removeView(mViews.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position)
            {
                View view = mViews.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1)
            {
                return arg0 == arg1;
            }

            @Override
            public int getCount()
            {
                return mViews.size();
            }
        };

        mViewPager.setAdapter(mAdapter);

    }

    private void initEvents()
    {
        mTabCallup.setOnClickListener(this);
        mTabSignup.setOnClickListener(this);
        mTabMine.setOnClickListener(this);
        mAddImg.setOnClickListener(this);

        mViewPager.setCurrentItem(0);
        mCallupImg.setImageResource(R.drawable.menu_click);

        mViewPager.setOnPageChangeListener(new OnPageChangeListener()
        {

            @Override
            public void onPageSelected(int arg0)
            {
                int currentItem = mViewPager.getCurrentItem();
                resetImg();
                switch (currentItem)
                {
                    case 0:
                        mCallupImg.setImageResource(R.drawable.menu_click);
                        //textView.setText("点名");
                        mAddImg.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mSignupImg.setImageResource(R.drawable.hand_click);
                        //textView.setText("签到");
                        mAddImg.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        mMineImg.setImageResource(R.drawable.mine_click);
                        //textView.setText("设置");
                        mAddImg.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2)
            {

            }

            @Override
            public void onPageScrollStateChanged(int arg0)
            {

            }
        });
    }

    @Override
    public void onClick(View v)
    {
        resetImg();
        switch (v.getId())
        {
            case R.id.id_tab_callup:
                mViewPager.setCurrentItem(0);
                mCallupImg.setImageResource(R.drawable.menu_click);
                //textView.setText("点名");
                mAddImg.setVisibility(View.VISIBLE);
                break;
            case R.id.id_tab_signup:
                mViewPager.setCurrentItem(1);
                mSignupImg.setImageResource(R.drawable.hand_click);
                //textView.setText("签到");
                mAddImg.setVisibility(View.VISIBLE);
                break;
            case R.id.id_tab_mine:
                mViewPager.setCurrentItem(2);
                mMineImg.setImageResource(R.drawable.mine_click);
                //textView.setText("设置");
                mAddImg.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 将所有的图片切换为暗色的
     */
    private void resetImg()
    {
        mCallupImg.setImageResource(R.drawable.menu);
        mSignupImg.setImageResource(R.drawable.hand);
        mMineImg.setImageResource(R.drawable.mine);
    }
}
