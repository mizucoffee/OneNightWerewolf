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
            new AlertDialog.Builder(getContext())
                    .setTitle("確認")
                    .setMessage("本当に" + activity.room.getPlayers().get(activity.room.getPlayerCount()) + "さんですか？")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            FragmentTransaction transaction = getFragmentManager().beginTransaction();
                            transaction.replace(R.id.fragment,new CardFragment());
                            transaction.commit();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        else{
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment,new CountFragment());
            transaction.commit();
        }



//        if(flag){
//
//            String seer = "";
//            for(int i:app.jinro.seer) seer = seer + i + ";";
//            if(!seer.equals(""))      seer = seer.substring(0,seer.length()-1);
//
//            String swap = "";
//            for(int i = 0; i != playersNum;i++)
//                if (app.jinro.cards.get(i) == Jinro.ROBBER) {
//                    swap = app.jinro.swapPlayer + "";
//                    break;
//                }
//
//            if (app.id != null) {
//                Http http = new Http();
//                http.setOnHttpResponseListener(new OnHttpResponseListener() {
//                    @Override
//                    public void onResponse(String response) {
//                        Intent intent = new Intent();
//                        intent.setClass(CheckNameFragment.this, CountActivity.class);
//                        startActivity(intent);
//                        finish();
//                    }
//                });
//                http.get("http://nuku.mizucoffee.net:1234/p3?id=" + app.id + "&seer="+seer+"&swap="+swap);
//            }
//
//        }else {
//            new AlertDialog.Builder(this)
//                    .setTitle("確認")
//                    .setMessage("本当に" + app.jinro.playerNames.get(count) + "さんですか？")
//                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent();
//                            intent.setClass(CheckNameFragment.this, CardActivity.class);
//                            intent.putExtra("card", count);
//                            startActivityForResult(intent, count);
//                        }
//                    })
//                    .setNegativeButton("No", null)
//                    .show();
//        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//        if (requestCode == count && resultCode == 1){
//            count++;
//            if (count < playersNum){
//                cpTv.setText(app.jinro.playerNames.get(count));
//            }else{
//                flag = true;
//                findViewById(R.id.sa).setVisibility(View.INVISIBLE);
//                findViewById(R.id.sb).setVisibility(View.INVISIBLE);
//
//                ((Button)findViewById(R.id.nextBtn)).setText("NEXT");
//
//                cpTv.setText("話し合い開始");
//            }
//        }
//    }
}
