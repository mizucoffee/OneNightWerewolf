package com.kawakawaplanning.onenightwerewolf;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    Typeface mMplusLight;
    Typeface mMplusThin;

    @BindView(R.id.startBtn)
    Button mStartBtn;
    @BindView(R.id.whatBtn)
    Button mWhatBtn;
    @BindView(R.id.settingBtn)
    Button mSettingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);

        mMplusLight = Typeface.createFromAsset(getAssets(), "mplus-1c-light.ttf");
        mMplusThin = Typeface.createFromAsset(getAssets(), "mplus-1c-thin.ttf");

        getActionBarTextView().setTypeface(mMplusLight);

        mSettingBtn.setTypeface(mMplusLight);
        mStartBtn.setTypeface(mMplusLight);
        mWhatBtn.setTypeface(mMplusLight);

    }

    private TextView getActionBarTextView() {
        TextView titleTextView = null;

        try {
            Field f = mToolBar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(mToolBar);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        return titleTextView;
    }
}
