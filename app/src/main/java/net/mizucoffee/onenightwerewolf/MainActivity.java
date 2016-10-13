package net.mizucoffee.onenightwerewolf;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

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

    MyApplication app;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //全てのアクティビティですべき処理 ↓
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (MyApplication)getApplicationContext();
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());

        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);
        //全てのアクティビティですべき処理 ↑

        mMplusLight = Typeface.createFromAsset(getAssets(), "mplus-1c-light.ttf");
        mMplusThin = Typeface.createFromAsset(getAssets(), "mplus-1c-thin.ttf");

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
        np.setMaxValue(6);
        ll.addView(np);

        new AlertDialog.Builder(this)
                .setTitle("Set the number of participants")
                .setCancelable(true)
                .setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        app.resetJinro();
                        app.jinro.setPlayersNum(np.getValue());

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,SetPositionActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("CANSEL", null)
                .setView(ll)
                .show();
    }

}
