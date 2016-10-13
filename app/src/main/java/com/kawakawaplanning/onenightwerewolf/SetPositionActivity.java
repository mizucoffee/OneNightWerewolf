package com.kawakawaplanning.onenightwerewolf;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

public class SetPositionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolBar;


    @BindView(R.id.werewolfTv)      TextView        werewolfTv;
    @BindView(R.id.seerTv)          TextView        seerTv;
    @BindView(R.id.robberTv)        TextView        robberTv;
    @BindView(R.id.minionTv)        TextView        minionTv;
    @BindView(R.id.tannerTv)        TextView        tannerTv;
    @BindView(R.id.villagerTv)      TextView        villagerTv;

    @BindView(R.id.werewolfSb)      SeekBar werewolfSb;
    @BindView(R.id.seerSb)          SeekBar seerSb;
    @BindView(R.id.robberSb)        SeekBar robberSb;
    @BindView(R.id.minionSb)        SeekBar minionSb;
    @BindView(R.id.tannerSb)        SeekBar tannerSb;

    int playersNum;

    int werewolf;
    int seer;
    int robber;
    int minion;
    int tanner;
    int villager;

    MyApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //全てのアクティビティですべき処理 ↓
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_position);
        app = (MyApplication)getApplicationContext();
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());

        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);
        //全てのアクティビティですべき処理 ↑

        playersNum = app.jinro.getPlayersNum();

        init();
    }

    public void init(){
        System.out.println(playersNum);
        switch (playersNum){
            case 3:
                werewolf = 0;seer = 1;robber = 1;minion = 0;tanner = 0;
                break;
            case 4:
                werewolf = 1;seer = 1;robber = 1;minion = 1;tanner = 0;
                break;
            case 5:
                werewolf = 1;seer = 1;robber = 1;minion = 1;tanner = 1;
                break;
            case 6:
                werewolf = 1;seer = 2;robber = 1;minion = 1;tanner = 1;
                break;
        }

        villager = playersNum - (werewolf + seer + robber + minion + tanner) + 1;

        werewolfTv.setText(":" + (werewolf + 1));
        seerTv.setText(":" + seer);
        robberTv.setText(":" + robber);
        minionTv.setText(":" + minion);
        tannerTv.setText(":" + tanner);
        villagerTv.setText(":" + villager);

        werewolfSb.setProgress(werewolf);
        seerSb.setProgress(seer);
        robberSb.setProgress(robber);
        minionSb.setProgress(minion);
        tannerSb.setProgress(tanner);

        werewolfSb.setOnSeekBarChangeListener(onChange);
        seerSb.setOnSeekBarChangeListener(onChange);
        robberSb.setOnSeekBarChangeListener(onChange);
        minionSb.setOnSeekBarChangeListener(onChange);
        tannerSb.setOnSeekBarChangeListener(onChange);
    }

    public SeekBar.OnSeekBarChangeListener onChange = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            //総人数がオーバーしないようにする処理
            int v = playersNum - (werewolfSb.getProgress() + seerSb.getProgress() + robberSb.getProgress() + minionSb.getProgress() + tannerSb.getProgress()) + 1;

//            if (v <= 0) {
//                werewolfSb.setEnabled(false);
//                seerSb.setEnabled(false);
//                robberSb.setEnabled(false);
//                minionSb.setEnabled(false);
//                tannerSb.setEnabled(false);
//            }

            if (v >= 0) {
                werewolf = werewolfSb.getProgress();
                seer     = seerSb.getProgress();
                robber   = robberSb.getProgress();
                minion   = minionSb.getProgress();
                tanner   = tannerSb.getProgress();
                villager = playersNum - (werewolf + seer + robber + minion + tanner) + 1;
            }else{
                werewolfSb.setProgress(werewolf);
                seerSb.setProgress(seer);
                robberSb.setProgress(robber);
                minionSb.setProgress(minion);
                tannerSb.setProgress(tanner);
            }

            werewolfTv.setText(":" + (werewolf + 1));
            seerTv    .setText(":" + seer);
            robberTv  .setText(":" + robber);
            minionTv  .setText(":" + minion);
            tannerTv  .setText(":" + tanner);
            villagerTv.setText(":" + villager);
        }
        @Override public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    @OnClick(R.id.nextBtn)
    public void next(){

        for(int i = 0;i <= 5; i++){
            switch (i){
                case 0:
                    for (int j = 0;j != (werewolf + 1);j++)
                        app.jinro.addCard(Jinro.WEREWOLF);
                    break;
                case 1:
                    for (int j = 0;j != seer;j++)
                        app.jinro.addCard(Jinro.SEER);
                    break;
                case 2:
                    for (int j = 0;j != robber;j++)
                        app.jinro.addCard(Jinro.ROBBER);
                    break;
                case 3:
                    for (int j = 0;j != minion;j++)
                        app.jinro.addCard(Jinro.MINION);
                    break;
                case 4:
                    for (int j = 0;j != tanner;j++)
                        app.jinro.addCard(Jinro.TANNER);
                    break;
                case 5:
                    for (int j = 0;j != villager;j++)
                        app.jinro.addCard(Jinro.VILLAGER);
                    break;
            }
        }

        app.jinro.shuffle();

        Intent intent = new Intent();
        intent.setClass(this,SetUsernameActivity.class);
        startActivity(intent);
        finish();
    }
}
