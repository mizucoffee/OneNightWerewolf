package net.mizucoffee.onenightwerewolf;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class ResultActivity extends AppCompatActivity {

    MyApplication app;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    @BindView(R.id.resultTv) TextView resultTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyApplication)getApplicationContext();
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_result);
        LinearLayout ll = (LinearLayout)findViewById(R.id.ll);

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
            TextView tv = new TextView(this);
            tv.setText(app.jinro.playerNames.get(i) + ": " + app.jinro.playerNames.get(app.jinro.poll.get(i)));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams1.setMargins(0, 0, 0, 48);

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

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams1.setMargins(0, 0, 0, 48);

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll2.addView(tv);
        }




        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);

        boolean flag = true;

        for(int i:app.jinro.killed){
            if(app.jinro.cards.get(i) == Jinro.TANNER){
                resultTv.setText("吊人勝利");
                flag = false;
            }
        }

        if (flag){
            for(int i:app.jinro.killed){
                if(app.jinro.cards.get(i) == Jinro.WEREWOLF){
                    resultTv.setText("村人勝利");
                    flag = false;
                }
            }
        }

        if (flag){
            resultTv.setText("人狼勝利");
            flag = false;
        }
//平和村
    }


}