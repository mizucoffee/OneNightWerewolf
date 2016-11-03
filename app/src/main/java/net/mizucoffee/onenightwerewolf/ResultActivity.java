package net.mizucoffee.onenightwerewolf;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;

import net.mizucoffee.onenightwerewolf.http.Http;
import net.mizucoffee.onenightwerewolf.http.OnHttpResponseListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class ResultActivity extends AppCompatActivity {

    MyApplication app;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.resultTv) TextView resultTv;
    int seerCount = 0;
    ArrayList<Integer> card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyApplication)getApplicationContext();
        Fabric.with(this, new Crashlytics());
        if(app.id != null) setTitle( getTitle() + " ID:" + app.id );
        setContentView(R.layout.activity_result);
        LinearLayout ll = (LinearLayout)findViewById(R.id.ll);

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
            TextView tv = new TextView(this);
            tv.setText(app.jinro.playerNames.get(i) + ": " + app.jinro.playerNames.get(app.jinro.poll.get(i)));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll.addView(tv);
        }

        LinearLayout ll2 = (LinearLayout)findViewById(R.id.ll2);

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
            TextView tv = new TextView(this);
            tv.setText(app.jinro.playerNames.get(i) + ": " + Jinro.getCardName(app.jinro.cards.get(i)));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll2.addView(tv);
        }

        LinearLayout ll3 = (LinearLayout)findViewById(R.id.ll3);

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {

            if(app.jinro.cards.get(i)==Jinro.ROBBER){
                TextView tv = new TextView(this);
                tv.setText(app.jinro.playerNames.get(i) + " <-入れ替え-> " + app.jinro.playerNames.get(app.jinro.swapPlayer));

                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 48);
                layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

                tv.setLayoutParams(layoutParams1);
                tv.setPadding(0,16,0,16);
                tv.setTextSize(24);
                ViewCompat.setElevation(tv,0);

                ll3.addView(tv);
            }
            if(app.jinro.cards.get(i)==Jinro.SEER){
                TextView tv = new TextView(this);
                if(app.jinro.seer.get(seerCount) == 10) {
                    tv.setText(app.jinro.playerNames.get(i) + " 占い-> 場のカード");
                }else {
                    tv.setText(app.jinro.playerNames.get(i) + " 占い-> " + app.jinro.playerNames.get(app.jinro.seer.get(seerCount)));
                }
                seerCount++;

                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 48);
                layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

                tv.setLayoutParams(layoutParams1);
                tv.setPadding(0,16,0,16);
                tv.setTextSize(24);
                ViewCompat.setElevation(tv,0);

                ll3.addView(tv);
            }

        }


        LinearLayout ll4 = (LinearLayout)findViewById(R.id.ll4);
        card = new ArrayList<>();

        for (int i:app.jinro.cards){
            card.add(i);
        }

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
            if(app.jinro.cards.get(i)==Jinro.ROBBER){
                card.set(i,app.jinro.cards.get(app.jinro.swapPlayer));
                card.set(app.jinro.swapPlayer,Jinro.ROBBER);
                break;
            }
        }

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
            TextView tv = new TextView(this);
            tv.setText(app.jinro.playerNames.get(i) + ": " + Jinro.getCardName(card.get(i)));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll4.addView(tv);
        }


        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);

        boolean flag = true;

        for(int i:app.jinro.killed){
            if(card.get(i) == Jinro.TANNER){
                resultTv.setText("吊人勝利");
                flag = false;
            }

            if(!flag)
                for(int j = 0;j != app.jinro.getPlayersNum();j++ )
                    if(card.get(j) == Jinro.TANNER)
                        app.jinro.point[j] = app.jinro.point[j] + 3;
        }

        if (flag){
            for(int i:app.jinro.killed)
                if(card.get(i) == Jinro.WEREWOLF){
                    resultTv.setText("村人勝利");
                    flag = false;
                }

            if(!flag)
                for(int j = 0;j != app.jinro.getPlayersNum();j++ )
                    if(card.get(j) == Jinro.VILLAGER || card.get(j) == Jinro.SEER || card.get(j) == Jinro.ROBBER)
                        app.jinro.point[j] = app.jinro.point[j] + 1;
        }

        if (flag){

            boolean notFoundJinro = true;
            for(int i = 0; i < app.jinro.getPlayersNum();i++)
                if(card.get(i) == Jinro.WEREWOLF)
                    notFoundJinro = false;

            if(notFoundJinro)
                if (app.jinro.killed.size() == 0){
                    resultTv.setText("平和村-村人勝利");
                    flag = false;
                }

            if(!flag)
                for(int j = 0;j != app.jinro.getPlayersNum();j++ )
                    if(card.get(j) == Jinro.VILLAGER || card.get(j) == Jinro.SEER || card.get(j) == Jinro.ROBBER)
                        app.jinro.point[j] = app.jinro.point[j] + 1;
        }

        if (flag){

            resultTv.setText("人狼勝利");
            flag = false;

            for(int j = 0;j != app.jinro.getPlayersNum();j++ )
                if(card.get(j) == Jinro.WEREWOLF || card.get(j) == Jinro.MINION)
                    app.jinro.point[j] = app.jinro.point[j] + 1;
        }

        String point = "";
        for(int i=0;i!=app.jinro.getPlayersNum();i++) point = point + app.jinro.point[i] + ";";
        if(!point.equals(""))      point = point.substring(0,point.length()-1);

        if (app.id != null) {
            Http http = new Http();
            try {
                http.get("http://nuku.mizucoffee.net:1234/p6?id=" + app.id + "&point="+ URLEncoder.encode(point,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        LinearLayout ll5 = (LinearLayout)findViewById(R.id.ll5);

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
            TextView tv = new TextView(this);
            tv.setText(app.jinro.playerNames.get(i) + ": " + app.jinro.point[i]);

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll5.addView(tv);
        }

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

        app.jinro.retry();
        app.jinro.shuffle();

        if (app.id != null) {
            Http http = new Http();
            http.setOnHttpResponseListener(new OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {
                    Intent intent = new Intent();
                    intent.setClass(ResultActivity.this, CardServeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            http.get("http://nuku.mizucoffee.net:1234/resetp2?id=" + app.id);
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