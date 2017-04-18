package net.mizucoffee.onenightwerewolf.jinro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.databinding.ActivityCheckNameBinding;

import java.lang.reflect.Field;

public class CheckNameFragment extends Fragment {

    boolean flag = false;

    FirebaseDatabase mDatabase;
    JinroActivity activity;
    DatabaseReference ref;
    ActivityCheckNameBinding mBinding;

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
        return inflater.inflate(R.layout.activity_check_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("room");
        mBinding = DataBindingUtil.bind(view);
        mBinding.setCard(this);

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

        TextView cpTv = (TextView)view.findViewById(R.id.playerCheckTV);
        if(activity.room.getPlayerCount() < activity.room.getPlayerNum())
            cpTv.setText(activity.room.getPlayers().get(activity.room.getPlayerCount()));
        else{
            view.findViewById(R.id.sa).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.sb).setVisibility(View.INVISIBLE);

            ((Button)view.findViewById(R.id.nextBtn)).setText("NEXT");

            cpTv.setText("話し合い開始");
        }

    }

    public void next(View v) {
        if(activity.room.getPlayerCount() < activity.room.getPlayerNum())
            new AlertDialog.Builder(activity)
                .setTitle("確認")
                    .setMessage("本当に" + activity.room.getPlayers().get(activity.room.getPlayerCount()) + "さんですか？")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(activity.room.getPhase() == 2){
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment,new CardFragment());
                                transaction.commit();
                            } else {
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment,new PollFragment());
                                transaction.commit();
                            }
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        else{
            activity.room.setPhase(3);
            activity.room.setPlayerCount(0);
            activity.send();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment,new CountFragment());
            transaction.commit();
        }
    }
}
