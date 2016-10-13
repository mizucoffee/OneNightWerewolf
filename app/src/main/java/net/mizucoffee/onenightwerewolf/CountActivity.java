package net.mizucoffee.onenightwerewolf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class CountActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    MyApplication app;

    @BindView(R.id.werewolf) TextView werewolf;
    @BindView(R.id.seer) TextView seer;
    @BindView(R.id.robber) TextView robber;
    @BindView(R.id.minion) TextView minion;
    @BindView(R.id.tanner) TextView tanner;
    @BindView(R.id.villager) TextView villager;

    @BindView(R.id.timerView) TextView timerView;

    CountDownTimer cdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        app = (MyApplication)getApplicationContext();
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());

        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);

        werewolf.setText("人狼:" + app.werewolf);
        seer.setText("占師:" + app.seer);
        robber.setText("怪盗:" + app.robber);
        minion.setText("怪盗:" + app.minion);
        tanner.setText("吊人:" + app.tanner);
        villager.setText("村人:" + app.villager);

        keyEventTimer = new CountDownTimer(1000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                pressed = false;
            }
        };

        cdt = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long l) {
                timerView.setText(Long.toString(l/1000/60) + ":" + Long.toString(l/1000%60));
            }

            @Override
            public void onFinish() {
                timerView.setText("そろそろ話し合いを終了してください");
            }
        };

        cdt.start();

    }

    @OnClick(R.id.nextBtn)
    public void next() {
        cdt.cancel();
        Intent intent = new Intent();
        intent.setClass(this, PollServeActivity.class);
        startActivity(intent);
        finish();
    }

    private CountDownTimer keyEventTimer;
    private boolean pressed = false;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if(!pressed) {
                keyEventTimer.cancel();
                keyEventTimer.start();
                Toast.makeText(this, "終了する場合は、もう一度バックボタンを押してください", Toast.LENGTH_SHORT).show();
                pressed = true;
                return false;
            }
            return super.dispatchKeyEvent(event);
        }
        return super.dispatchKeyEvent(event);
    }
}
