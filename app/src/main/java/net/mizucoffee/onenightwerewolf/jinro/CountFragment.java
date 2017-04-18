package net.mizucoffee.onenightwerewolf.jinro;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.databinding.ActivityCountBinding;

import java.lang.reflect.Field;

public class CountFragment extends Fragment {

    CountDownTimer cdt;

    FirebaseDatabase mDatabase;
    JinroActivity activity;
    DatabaseReference ref;
    ActivityCountBinding mBinding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (JinroActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.activity_count, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("room");
        mBinding = DataBindingUtil.bind(view);
        mBinding.setCount(this);

        activity.setSupportActionBar(mBinding.toolbar);

        Typeface mplusLight = Typeface.createFromAsset(getContext().getAssets(), "mplus-1c-light.ttf");
        TextView titleTextView = null;

        try {
            Field f = mBinding.toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(mBinding.toolbar);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (titleTextView != null) titleTextView.setTypeface(mplusLight);

        mBinding.werewolf.setText("人狼:" + activity.room.getWerewolf());
        mBinding.seer.setText("占師:" + activity.room.getSeerc());
        mBinding.robber.setText("怪盗:" + activity.room.getRobber());
        mBinding.minion.setText("狂人:" + activity.room.getMinion());
        mBinding.tanner.setText("吊人:" + activity.room.getTanner());
        mBinding.villager.setText("村人:" + activity.room.getVillager());

        cdt = new CountDownTimer(180000, 1000) {
            @Override
            public void onTick(long l) {
                mBinding.timerView.setText(String.format("%02d",l/1000/60) + ":" + String.format("%02d",l/1000%60));
                activity.room.setCountDown((int)l/1000);
                activity.send();
            }

            @Override
            public void onFinish() {
                mBinding.timerView.setText("話し合い終了");
            }
        };

        cdt.start();

    }

    public void next(View v) {
        cdt.cancel();

        activity.room.setPhase(4);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment,new CheckNameFragment());
        transaction.commit();
    }

}


