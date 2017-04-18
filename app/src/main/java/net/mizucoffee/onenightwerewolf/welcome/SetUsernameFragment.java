package net.mizucoffee.onenightwerewolf.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.Room;
import net.mizucoffee.onenightwerewolf.databinding.FragmentSetUsernameBinding;
import net.mizucoffee.onenightwerewolf.jinro.JinroActivity;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SetUsernameFragment extends Fragment{

    AppCompatEditText editText[];

//    @BindView(R.id.ll) LinearLayout ll;
//    @BindView(R.id.root) LinearLayout root;
//    @BindView(R.id.toolbar) Toolbar mToolBar;

    WelcomeActivity activity;
    FirebaseDatabase mDatabase;
    FragmentSetUsernameBinding mBinding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (WelcomeActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_set_username, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabase = FirebaseDatabase.getInstance();
        ref = mDatabase.getReference("room");
        mBinding = DataBindingUtil.bind(view);
        mBinding.setUsername(this);

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
        editText = new AppCompatEditText[activity.room.getPlayerNum()];

        for (int i = 0; i != activity.room.getPlayerNum(); i++) {
            editText[i] = new AppCompatEditText(getContext());
            LinearLayout l = new LinearLayout(getContext());
            TextView t = new TextView(getContext());
            t.setPadding(0,0,16,0);
            t.setText("Player "+ (i + 1) );
            editText[i].setHint("Player "+ (i + 1));
            editText[i].setInputType(InputType.TYPE_CLASS_TEXT);
            l.addView(t,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            l.addView(editText[i],new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mBinding.ll.addView(l);
            editText[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    ArrayList<String> players = new ArrayList<>();
                    for (EditText et : editText) {
                        if (et.getText().toString().equals("")) {
                            players.add(et.getHint().toString());
                        } else {
                            players.add(et.getText().toString());
                        }
                    }
                    activity.room.setPlayers(players);

                    activity.send();
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
    }

    DatabaseReference ref;
    public void next(View v){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("読込中");
        progressDialog.setMessage("しばらくお待ち下さい...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        List<String> l = new ArrayList<String>();

        for(EditText et:editText){
            if(et.getText().toString().equals("")){
                l.add(et.getHint().toString());
            } else {
                l.add(et.getText().toString());
            }
        }

        boolean flag = true;
        for(String s: l){
            if (s.contains("場の"))
                flag = false;
        }

        if(!overlapCheck(l)){
            if(flag) {
                ArrayList<String> players = new ArrayList<>();
                for (EditText et : editText) {
                    if (et.getText().toString().equals("")) {
                        players.add(et.getHint().toString());
                    } else {
                        players.add(et.getText().toString());
                    }
                }
                activity.room.setPlayers(players);
                activity.room.setPhase(2);

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        GenericTypeIndicator<ArrayList<Room>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Room>>() {};
                        ArrayList<Room> list = dataSnapshot.getValue(genericTypeIndicator);
                        if(list == null) list = new ArrayList<>();
                        for(int i = 0;i != list.size();i++)
                            if(list.get(i).getRoomId().equals(activity.room.getRoomId()))
                                list.set(i,activity.room);

                        ref.setValue(list);

                        progressDialog.hide();

                        Intent intent = new Intent();
                        intent.setClass(getContext(),JinroActivity.class);
                        intent.putExtra("room",new Gson().toJson(activity.room));
                        startActivity(intent);

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment,new WelcomeFragment());
                        transaction.commit();

                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        for(int i = 0; i < fm.getBackStackEntryCount(); ++i)
                            fm.popBackStack();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        databaseError.toException().printStackTrace();
                        System.out.println("error"+databaseError.getDetails());
                        progressDialog.hide();
                        Toast.makeText(getContext(),"エラーが発生しました。",Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                Snackbar snackbar = Snackbar.make(mBinding.root, "使用できない文字列が含まれています。変更してください。", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }else{
            Snackbar snackbar = Snackbar.make(mBinding.root, "名前が重複しています。変更してください。", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    public static Boolean overlapCheck(List<String> checkList){
        Boolean result = false;
        Set<String> checkHash = new HashSet<String>();
        for(String str : checkList) {
            if(checkHash.contains(str)) {
                result = true;
                break;
            } else {
                checkHash.add(str);
            }
        }
        return result;
    }
}