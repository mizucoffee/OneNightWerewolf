package net.mizucoffee.onenightwerewolf.jinro;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;

import net.mizucoffee.onenightwerewolf.Jinro;
import net.mizucoffee.onenightwerewolf.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ResultFragment extends Fragment {

    private FirebaseDatabase mDatabase;
    JinroActivity activity;
    TextView resultTv;

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
        return inflater.inflate(R.layout.activity_result, container, false);
    }

    Toolbar toolbar;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance();
        resultTv = (TextView)view.findViewById(R.id.resultTv);

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

        LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);

        for (int i = 0; i != activity.room.getPlayerNum(); i++) {
            TextView tv = new TextView(getContext());
            tv.setText(activity.room.getPlayers().get(i) + ": " + activity.room.getPlayers().get(activity.room.getPoll().get(i)));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll.addView(tv);
        }

        LinearLayout ll2 = (LinearLayout)view.findViewById(R.id.ll2);

        for (int i = 0; i != activity.room.getPlayerNum(); i++) {
            TextView tv = new TextView(getContext());
            tv.setText(activity.room.getPlayers().get(i) + ": " + Jinro.getCardName(activity.room.getCards().get(i)));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll2.addView(tv);
        }

        LinearLayout ll3 = (LinearLayout)view.findViewById(R.id.ll3);

        int seer = 0;
        int robber = 0;
        for (int i = 0; i != activity.room.getPlayerNum(); i++) {

            if(activity.room.getCards().get(i)==Jinro.ROBBER){
                TextView tv = new TextView(getContext());
                tv.setText(activity.room.getPlayers().get(i) + " <-入れ替え-> " + activity.room.getPlayers().get(activity.room.getSwap().get(robber)));

                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 48);
                layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

                tv.setLayoutParams(layoutParams1);
                tv.setPadding(0,16,0,16);
                tv.setTextSize(24);
                ViewCompat.setElevation(tv,0);

                ll3.addView(tv);
                robber++;
            }
            if(activity.room.getCards().get(i)==Jinro.SEER){
                TextView tv = new TextView(getContext());
                if(activity.room.getSeer().get(seer) == 10)
                    tv.setText(activity.room.getPlayers().get(i) + " 占い-> 場のカード");
                else
                    tv.setText(activity.room.getPlayers().get(i) + " 占い-> " + activity.room.getPlayers().get(activity.room.getSeer().get(seer)));

                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 48);
                layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

                tv.setLayoutParams(layoutParams1);
                tv.setPadding(0,16,0,16);
                tv.setTextSize(24);
                ViewCompat.setElevation(tv,0);

                ll3.addView(tv);
                seer++;
            }

        }

        robber = 0;
        LinearLayout ll4 = (LinearLayout)view.findViewById(R.id.ll4);
        ArrayList<Integer> card = new ArrayList<>();

        for (int i:activity.room.getCards()) card.add(i);

        for (int i = 0; i != activity.room.getPlayerNum(); i++) {
            if(activity.room.getCards().get(i)==Jinro.ROBBER){
                card.set(i, activity.room.getCards().get(activity.room.getSwap().get(robber)));
                card.set(activity.room.getSwap().get(robber),Jinro.ROBBER);
                robber++;
            }
        }

        for (int i = 0; i != activity.room.getPlayerNum(); i++) {
            TextView tv = new TextView(getContext());
            tv.setText(activity.room.getPlayers().get(i) + ": " + Jinro.getCardName(card.get(i)));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll4.addView(tv);
        }

        ArrayList<Integer> points = activity.room.getPoint();
        if(points == null) {
            points = new ArrayList<>();
            for(String s:activity.room.getPlayers()) points.add(0);
        }

        switch (Jinro.checkWinner(activity.room.getPoll(),activity.room.getCards(),activity.room.getSwap())){
            case Jinro.VILLAGER_NORMAL:
                resultTv.setText("村人勝利");
                for (int i = 0; i != activity.room.getPlayerNum(); i++)
                    if(card.get(i) == Jinro.VILLAGER || card.get(i) == Jinro.SEER || card.get(i) == Jinro.ROBBER)
                        points.set(i,points.get(i) + 1);
                break;
            case Jinro.VILLAGER_HEIWA:
                resultTv.setText("村人勝利 - 平和村");
                for (int i = 0; i != activity.room.getPlayerNum(); i++)
                    if(card.get(i) == Jinro.VILLAGER || card.get(i) == Jinro.SEER || card.get(i) == Jinro.MINION || card.get(i) == Jinro.ROBBER)
                        points.set(i,points.get(i) + 2);
                break;
            case Jinro.VILLAGER_TANNER:
                resultTv.setText("村人勝利 - 吊人回避");
                for (int i = 0; i != activity.room.getPlayerNum(); i++)
                    if(card.get(i) == Jinro.VILLAGER || card.get(i) == Jinro.SEER || card.get(i) == Jinro.MINION || card.get(i) == Jinro.ROBBER)
                        points.set(i,points.get(i) + 1);
                break;
            case Jinro.WEREWOLF_NORMAL:
                resultTv.setText("人狼勝利");
                for (int i = 0; i != activity.room.getPlayerNum(); i++)
                    if(card.get(i) == Jinro.WEREWOLF || card.get(i) == Jinro.MINION)
                        points.set(i,points.get(i) + 1);
                break;
            case Jinro.WEREWOLF_MINION:
                resultTv.setText("人狼勝利 - 狂人死亡");
                for (int i = 0; i != activity.room.getPlayerNum(); i++)
                    if(card.get(i) == Jinro.WEREWOLF)
                        points.set(i,points.get(i) + 1);
                for (int i = 0; i != activity.room.getPlayerNum(); i++)
                    if(card.get(i) == Jinro.MINION)
                        points.set(i,points.get(i) + 2);
                break;
            case Jinro.TANNER_NORMAL:
                resultTv.setText("吊人勝利");
                for (int i = 0; i != activity.room.getPlayerNum(); i++)
                    if(card.get(i) == Jinro.TANNER)
                        points.set(i,points.get(i) + 3);
                break;
        }
        activity.room.setPoint(points);
        activity.room.setPhase(6);
        activity.send();

        LinearLayout ll5 = (LinearLayout)view.findViewById(R.id.ll5);

        for (int i = 0; i != activity.room.getPlayerNum(); i++) {
            TextView tv = new TextView(getContext());
            tv.setText(activity.room.getPlayers().get(i) + ": " + activity.room.getPoint().get(i));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 48);
            layoutParams1.gravity = Gravity.CENTER_HORIZONTAL;

            tv.setLayoutParams(layoutParams1);
            tv.setPadding(0,16,0,16);
            tv.setTextSize(24);
            ViewCompat.setElevation(tv,0);

            ll5.addView(tv);
        }

        view.findViewById(R.id.nextBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.refresh();
                activity.room.setPhase(2);
                activity.send();

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,new CheckNameFragment());
                transaction.commit();
            }
        });
    }
}