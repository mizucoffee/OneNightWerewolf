package net.mizucoffee.onenightwerewolf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class PollServeActivity extends AppCompatActivity {

    int playersNum;
    MyApplication app;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    TextView cpTv;

    int count = 0;

    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        app = (MyApplication)getApplicationContext();
        Fabric.with(this, new Crashlytics());


        playersNum = app.jinro.getPlayersNum();


        setContentView(R.layout.activity_check_name);
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);

        cpTv = (TextView)findViewById(R.id.playerCheckTV);
        cpTv.setText(app.jinro.playerNames.get(count));

        keyEventTimer = new CountDownTimer(1000, 100) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                pressed = false;
            }
        };

    }

    @OnClick(R.id.nextBtn)
    public void next() {

        if(flag){
            Intent intent = new Intent();
            intent.setClass(this, KillActivity.class);
            startActivity(intent);
            finish();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("確認")
                    .setMessage("本当に" + app.jinro.playerNames.get(count) + "さんですか？")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setClass(PollServeActivity.this, PollActivity.class);
                            intent.putExtra("card", count);
                            startActivityForResult(intent, count);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == count && resultCode == 1){
            count++;
            if (count < playersNum){
                cpTv.setText(app.jinro.playerNames.get(count));
            }else{
                flag = true;
                findViewById(R.id.sa).setVisibility(View.INVISIBLE);
                findViewById(R.id.sb).setVisibility(View.INVISIBLE);

                ((Button)findViewById(R.id.nextBtn)).setText("NEXT");

                cpTv.setText("結果発表");
            }
        }
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
