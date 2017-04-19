package net.mizucoffee.onenightwerewolf.jinro;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.mizucoffee.onenightwerewolf.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class PollFragment extends Fragment {

    Button[] btn;

    FirebaseDatabase mDatabase;
    JinroActivity activity;
    DatabaseReference ref;

    Toolbar toolbar;

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
        return inflater.inflate(R.layout.activity_poll, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("room");

        toolbar = (Toolbar)view.findViewById(R.id.toolbar);

        activity.setSupportActionBar(toolbar);

        Typeface mplusLight = Typeface.createFromAsset(getContext().getAssets(), "mplus-1c-light.ttf");
        TextView titleTextView = null;

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (titleTextView != null) titleTextView.setTypeface(mplusLight);


        btn = new Button[activity.room.getPlayerNum()];
        LinearLayout lll = (LinearLayout)view.findViewById(R.id.ll);
        for (int i = 0; i != activity.room.getPlayerNum(); i++) {
            if(i == activity.room.getPlayerCount()) continue;
            btn[i] = new Button(getContext());
            btn[i].setText(activity.room.getPlayers().get(i));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams1.setMargins(0, 0, 0, 48);

            btn[i].setLayoutParams(layoutParams1);
            btn[i].setPadding(0,16,0,16);
            btn[i].setBackgroundResource(R.drawable.next_btn_back);
            btn[i].setTextSize(24);
            btn[i].setTextColor(Color.WHITE);
            btn[i].setOnClickListener(poll);
            ViewCompat.setElevation(btn[i],0);

            lll.addView(btn[i]);

        }
    }

    View.OnClickListener next = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activity.room.setPlayerCount(activity.room.getPlayerCount() + 1);

            if(activity.room.getPlayerCount() == activity.room.getPlayerNum()){
                activity.room.setPlayerCount(0);
                activity.send();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,new KillFragment());//結果を表示する
                transaction.commit();
            } else {
                activity.send();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,new CheckNameFragment());
                transaction.commit();
            }
        }
    };

    View.OnClickListener poll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = 0;
            for(String s:activity.room.getPlayers()) {
                if(s.equals(((Button)view).getText())){
                    break;
                }
                i++;
            }

            ArrayList<Integer> list = activity.room.getPoll();
            if(list == null) list = new ArrayList<>();
            list.add(i);

            activity.room.setPoll(list);
            next.onClick(null);
        }
    };

}