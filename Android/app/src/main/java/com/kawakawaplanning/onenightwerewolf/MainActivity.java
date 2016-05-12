package com.kawakawaplanning.onenightwerewolf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        setSupportActionBar(mToolBar);

        mMplusLight = Typeface.createFromAsset(getAssets(), "mplus-1c-light.ttf");
        mMplusThin = Typeface.createFromAsset(getAssets(), "mplus-1c-thin.ttf");

        getActionBarTextView().setTypeface(mMplusLight);

        mSettingBtn.setTypeface(mMplusLight);
        mStartBtn.setTypeface(mMplusLight);
        mWhatBtn.setTypeface(mMplusLight);

    }

    @OnClick(R.id.startBtn)
    public void startBtn(){
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        final NumberPicker np = new NumberPicker(this);
        np.setMinValue(3);
        np.setMaxValue(10);
        ll.addView(np);

        new AlertDialog.Builder(this)
                .setTitle("Set the number of participants")
                .setCancelable(true)
                .setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,SetPositionActivity.class);
                        intent.putExtra("number",np.getValue());
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANSEL", null)
                .setView(ll)
                .show();
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
