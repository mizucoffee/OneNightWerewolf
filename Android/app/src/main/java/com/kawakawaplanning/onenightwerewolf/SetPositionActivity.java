package com.kawakawaplanning.onenightwerewolf;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.SeekBar;
import android.widget.TextView;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetPositionActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolBar;
    Typeface mMplusLight;
    Typeface mMplusThin;

    @BindView(R.id.werewolfTv)      TextView        werewolfTv;
    @BindView(R.id.seerTv)          TextView        seerTv;
    @BindView(R.id.robberTv)        TextView        robberTv;
    @BindView(R.id.minionTv)        TextView        minionTv;
    @BindView(R.id.tannerTv)        TextView        tannerTv;
    @BindView(R.id.villagerTv)      TextView        villagerTv;

    @BindView(R.id.werewolfSb)      DiscreteSeekBar werewolfSb;
    @BindView(R.id.seerSb)          DiscreteSeekBar seerSb;
    @BindView(R.id.robberSb)        DiscreteSeekBar robberSb;
    @BindView(R.id.minionSb)        DiscreteSeekBar minionSb;
    @BindView(R.id.tannerSb)        DiscreteSeekBar tannerSb;

    int count;

    int card;

    int werewolf;
    int seer;
    int robber;
    int minion;
    int tanner;
    int villager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_position);
        ButterKnife.bind(this);

        setSupportActionBar(mToolBar);

        mMplusLight = Typeface.createFromAsset(getAssets(), "mplus-1c-light.ttf");
        mMplusThin = Typeface.createFromAsset(getAssets(), "mplus-1c-thin.ttf");

        getActionBarTextView().setTypeface(mMplusLight);

        count = getIntent().getIntExtra("number",0);
        card  = count + 2;
        if (count == 0) finish();

        init();

        werewolfSb.setOnProgressChangeListener(onChange);
        seerSb.setOnProgressChangeListener(onChange);
        robberSb.setOnProgressChangeListener(onChange);
        minionSb.setOnProgressChangeListener(onChange);
        tannerSb.setOnProgressChangeListener(onChange);
    }

    public DiscreteSeekBar.OnProgressChangeListener onChange = new DiscreteSeekBar.OnProgressChangeListener() {
        @Override
        public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

            int v = count - (werewolfSb.getProgress() + seerSb.getProgress() + robberSb.getProgress() + minionSb.getProgress() + tannerSb.getProgress());

            if (v >= 0) {
                werewolf = werewolfSb.getProgress();
                seer     = seerSb.getProgress();
                robber   = robberSb.getProgress();
                minion   = minionSb.getProgress();
                tanner   = tannerSb.getProgress();
                villager = count - (werewolf + seer + robber + minion + tanner);
            }else{
                werewolfSb.setProgress(werewolf);
                seerSb.setProgress(seer);
                robberSb.setProgress(robber);
                minionSb.setProgress(minion);
                tannerSb.setProgress(tanner);
            }

            werewolfTv.setText(":" + werewolf);
            seerTv    .setText(":" + seer);
            robberTv  .setText(":" + robber);
            minionTv  .setText(":" + minion);
            tannerTv  .setText(":" + tanner);
            villagerTv.setText(":" + villager);
        }

        @Override public void onStartTrackingTouch(DiscreteSeekBar seekBar) {}
        @Override public void onStopTrackingTouch(DiscreteSeekBar seekBar) {}
    };

    public void init(){
        switch (count){
            case 3:
                werewolf = 1;seer = 1;robber = 1;minion = 0;tanner = 0;
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
            case 7:
                werewolf = 1;seer = 2;robber = 1;minion = 1;tanner = 1;
                break;
            case 8:
                werewolf = 1;seer = 2;robber = 1;minion = 2;tanner = 1;
                break;
            case 9:
                werewolf = 2;seer = 2;robber = 1;minion = 2;tanner = 2;
                break;
            case 10:
                werewolf = 2;seer = 2;robber = 1;minion = 2;tanner = 2;
                break;
        }

        villager = count - (werewolf + seer + robber + minion + tanner);

        werewolfTv.setText(":" + werewolf);
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
