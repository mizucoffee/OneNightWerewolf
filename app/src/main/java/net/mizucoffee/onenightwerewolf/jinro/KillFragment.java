package net.mizucoffee.onenightwerewolf.jinro;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.mizucoffee.onenightwerewolf.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class KillFragment extends Fragment {

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
        return inflater.inflate(R.layout.activity_check_name, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

        ArrayList<Integer> max = new ArrayList<>();

        int maxNum = 0;

        for (int i = 0; i < activity.room.getPoll().size(); i++) {
            int value = activity.room.getPoll().get(i); /* 名前つけただけ */
            int count = 0; /* valueの出現回数 */
            int j;

            for (j = i; j < activity.room.getPoll().size(); j++)
                if (activity.room.getPoll().get(j) == value)
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

        TextView sa = (TextView) view.findViewById(R.id.sa);
        TextView playerTv = (TextView) view.findViewById(R.id.playerCheckTV);
        TextView sb = (TextView) view.findViewById(R.id.sb);


        if (max.size() == activity.room.getPlayerNum()){
            sa.setText("本日は");
            playerTv.setText("処刑者が0人");
            sb.setText("でした");
            activity.room.setKill(null);
            activity.room.setPhase(5);
            activity.send();
        } else {

            String name = "";
            ArrayList<Integer> list = new ArrayList<>();
            for (int i : max) {
                list.add(i);
                name = name + activity.room.getPlayers().get(i) + "さんと";
            }
            activity.room.setKill(list);
            activity.room.setPhase(5);
            activity.send();
            name = name.substring(0, name.length() - 1);

            sa.setText("本日処刑されるのは");
            playerTv.setText(name);
            sb.setText("です");

        }
        ((Button)view.findViewById(R.id.nextBtn)).setText("NEXT");
        ((Button)view.findViewById(R.id.nextBtn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,new ResultFragment());
                transaction.commit();
            }
        });
    }
}