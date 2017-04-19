package net.mizucoffee.onenightwerewolf.jinro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import net.mizucoffee.onenightwerewolf.Jinro;
import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.Room;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CardFragment extends Fragment{

    Button[] btn;
    Button nextBtn;

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
        int layout = R.layout.activity;
        switch (activity.room.getCards().get(activity.room.getPlayerCount())){
            case Jinro.WEREWOLF: layout = R.layout.activity_card_jinro;    break;
            case Jinro.SEER: layout = R.layout.activity_card_seer;     break;
            case Jinro.MINION: layout = R.layout.activity_card_minion;   break;
            case Jinro.ROBBER: layout = R.layout.activity_card_robber;   break;
            case Jinro.TANNER: layout = R.layout.activity_card_tanner;   break;
            case Jinro.VILLAGER: layout = R.layout.activity_card_villager; break;
        }
        return inflater.inflate(layout, container, false);
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

        switch (activity.room.getCards().get(activity.room.getPlayerCount())){

            case Jinro.SEER:
                LinearLayout ll = (LinearLayout)view.findViewById(R.id.ll);

                btn = new Button[activity.room.getPlayerNum() + 1];

                for (int i = 0; i != activity.room.getPlayerNum(); i++) {
                    if(i == activity.room.getPlayerCount()) continue;
                    btn[i] = new Button(getContext());
                    btn[i].setText(activity.room.getPlayers().get(i));

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(0, 0, 0, 48);

                    btn[i].setLayoutParams(layoutParams);
                    btn[i].setPadding(0,16,0,16);
                    btn[i].setBackgroundResource(R.drawable.next_btn_back);
                    btn[i].setTextSize(24);
                    btn[i].setTextColor(Color.WHITE);
                    btn[i].setOnClickListener(seer);
                    ViewCompat.setElevation(btn[i],0);

                    ll.addView(btn[i]);
                }

                btn[activity.room.getPlayerNum()] = new Button(getContext());
                btn[activity.room.getPlayerNum()].setText("場のカードを見る");

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, 48);

                btn[activity.room.getPlayerNum()].setLayoutParams(layoutParams);
                btn[activity.room.getPlayerNum()].setPadding(0,16,0,16);
                btn[activity.room.getPlayerNum()].setBackgroundResource(R.drawable.next_btn_back);
                btn[activity.room.getPlayerNum()].setTextSize(24);
                btn[activity.room.getPlayerNum()].setTextColor(Color.WHITE);
                btn[activity.room.getPlayerNum()].setOnClickListener(seer);
                ViewCompat.setElevation(btn[activity.room.getPlayerNum()],0);

                ll.addView(btn[activity.room.getPlayerNum()]);

                break;
            case Jinro.ROBBER:
                LinearLayout lll = (LinearLayout)view.findViewById(R.id.ll);

                btn = new Button[activity.room.getPlayerNum()];

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
                    btn[i].setOnClickListener(robber);
                    ViewCompat.setElevation(btn[i],0);

                    lll.addView(btn[i]);

                }
                break;
            case Jinro.WEREWOLF:
                int count = 0;
                for(int i:activity.room.getCards()) if(i == Jinro.WEREWOLF) count ++;
                if(count >= 2) {
                    TextView tv = (TextView) view.findViewById(R.id.textView5);
                    for (int i = 0; i != activity.room.getPlayerNum(); i++) {
                        if (i == activity.room.getPlayerCount())
                            continue;
                        if (activity.room.getCards().get(i) == Jinro.WEREWOLF) {
                            tv.setText(activity.room.getPlayers().get(i) + "さんも人狼です");
                            break;
                        }
                    }
                }
            case Jinro.TANNER:
            case Jinro.MINION:
            case Jinro.VILLAGER:
                nextBtn = (Button)view.findViewById(R.id.nextBtn);
                nextBtn.setOnClickListener(next);
                break;
        }
    }

    View.OnClickListener next = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            activity.room.setPlayerCount(activity.room.getPlayerCount() + 1);
            activity.send();
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment,new CheckNameFragment());
            transaction.commit();
        }
    };

    View.OnClickListener seer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println(((Button)view).getText());
            if(((Button)view).getText().equals("場のカードを見る")){

                ArrayList<Integer> list = activity.room.getSeer();
                if(list == null) list = new ArrayList<>();
                list.add(10);
                activity.room.setSeer(list);
                new AlertDialog.Builder(getContext())
                        .setTitle("占い結果")
                        .setMessage("場のカードは" + Jinro.getCardName(activity.room.getCards().get(activity.room.getPlayerNum())) + "と" + Jinro.getCardName(activity.room.getCards().get(activity.room.getPlayerNum() + 1)) + "でした。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                next.onClick(null);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }else{
                int i = 0;
                for(String s:activity.room.getPlayers()) {
                    if(s.equals(((Button)view).getText())) break;
                    i++;
                }
                ArrayList<Integer> list = activity.room.getSeer();
                if(list == null) list = new ArrayList<>();
                list.add(i);
                activity.room.setSeer(list);
                new AlertDialog.Builder(getContext())
                        .setTitle("占い結果")
                        .setMessage((((Button)view).getText())+"さんは" + Jinro.getCardName(activity.room.getCards().get(i)) + "でした。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                next.onClick(null);
                            }
                        })
                        .setCancelable(false)
                        .show();
            }ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<ArrayList<Room>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Room>>() {};
                    ArrayList<Room> list = dataSnapshot.getValue(genericTypeIndicator);
                    if(list == null) list = new ArrayList<>();
                    for(int i = 0;i != list.size();i++)
                        if(list.get(i).getRoomId().equals(activity.room.getRoomId()))
                            list.set(i,activity.room);

                    ref.setValue(list);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                    System.out.println("error"+databaseError.getDetails());
                }
            });
        }
    };

    View.OnClickListener robber = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = 0;
            for(String s:activity.room.getPlayers()) {
                if(s.equals(((Button)view).getText())) break;
                i++;
            }

            ArrayList<Integer> list = activity.room.getSwap();
            if(list == null) list = new ArrayList<>();
            list.add(i);
            activity.room.setSwap(list);
            new AlertDialog.Builder(getContext())
                    .setTitle("入れ替え結果")
                    .setMessage((((Button)view).getText())+"さんと入れ替わりました。あなたは" + Jinro.getCardName(activity.room.getCards().get(i)) + "になりました。")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            next.onClick(null);
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    };

}