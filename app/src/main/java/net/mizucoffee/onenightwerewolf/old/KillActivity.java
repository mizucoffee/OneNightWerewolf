package net.mizucoffee.onenightwerewolf.old;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import net.mizucoffee.onenightwerewolf.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class KillActivity extends AppCompatActivity {

    MyApplication app;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    int player_poll[];
    int playersNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyApplication)getApplicationContext();
        Fabric.with(this, new Crashlytics());

        playersNum = app.jinro.getPlayersNum();
        player_poll = new int[playersNum];
        if(app.id != null) setTitle( getTitle() + " ID:" + app.id );
        setContentView(R.layout.activity_check_name);



        playersNum = app.jinro.getPlayersNum();
        player_poll = new int[playersNum];

        int ii = 0;
        for (int i : app.jinro.poll) {
            player_poll[ii] = i;
            ii++;
        }

        ArrayList<Integer> max = new ArrayList<>();

        int maxNum = 0;

        for (int i = 0; i < player_poll.length; i++) {
            int value = player_poll[i]; /* 名前つけただけ */
            int count = 0; /* valueの出現回数 */
            int j;

            for (j = i; j < player_poll.length; j++)
                if (player_poll[j] == value)
                    count++;

            if (count > maxNum) {
                maxNum = count;
                max = new ArrayList<>();
                max.add(value);
            }

            if (count == maxNum) {
                boolean flag = true;
                for (int k : max) {
                    if (value == k) flag = false;
                }
                if (flag) max.add(value);
            }
        }

        if (max.size() == app.jinro.getPlayersNum()){
            ((TextView) findViewById(R.id.sa)).setText("本日は");
            ((TextView) findViewById(R.id.playerCheckTV)).setText("処刑者が0人");
            ((TextView) findViewById(R.id.sb)).setText("でした");
        }else {

            String name = "";
            for (int i : max) {
                app.jinro.killed.add(i);
                name = name + app.jinro.playerNames.get(i) + "さんと";
            }
            name = name.substring(0, name.length() - 1);

            ((TextView) findViewById(R.id.sa)).setText("本日処刑されるのは");
            ((TextView) findViewById(R.id.playerCheckTV)).setText(name);
            ((TextView) findViewById(R.id.sb)).setText("です");

        }
        ((Button) findViewById(R.id.nextBtn)).setText("NEXT");
        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);
    }
    @OnClick(R.id.nextBtn)
    public void next() {

        Intent intent = new Intent();
        intent.setClass(KillActivity.this, ResultActivity.class);
        startActivity(intent);
        finish();

    }

}