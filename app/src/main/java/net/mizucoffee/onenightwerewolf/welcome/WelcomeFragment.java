package net.mizucoffee.onenightwerewolf.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.Room;
import net.mizucoffee.onenightwerewolf.databinding.FragmentWelcomeBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class WelcomeFragment extends Fragment {

    FragmentWelcomeBinding mBinding;
    private FirebaseDatabase mDatabase;
    WelcomeActivity activity;

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
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding = DataBindingUtil.bind(getView());
        mBinding.setWelcome(this);
        activity.room = new Room();
        activity.setSupportActionBar(mBinding.toolbar);
        mDatabase = FirebaseDatabase.getInstance();

        Typeface mplusLight =  Typeface.createFromAsset(getContext().getAssets(), "mplus-1c-light.ttf");;
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
        if(titleTextView != null) titleTextView.setTypeface(mplusLight);
        activity.setTitle("OneNightWerewolf");

        mBinding.settingBtn.setTypeface(mplusLight);
        mBinding.startBtn.setTypeface(mplusLight);
        mBinding.whatBtn.setTypeface(mplusLight);
    }

    private String newId;
    public void start(View v){
        LinearLayout ll = new LinearLayout(getContext());
        ll.setOrientation(LinearLayout.VERTICAL);

        final NumberPicker np = new NumberPicker(getContext());
        np.setMinValue(3);
        np.setMaxValue(6);
        ll.addView(np);

        new AlertDialog.Builder(getContext())
                .setTitle("プレイ人数")
                .setCancelable(true)
                .setPositiveButton("NEXT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("準備中");
                        progressDialog.setMessage("しばらくお待ち下さい...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.setCancelable(false);
                                            progressDialog.show();

                        final DatabaseReference ref = mDatabase.getReference("room");
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                GenericTypeIndicator<ArrayList<Room>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Room>>() {};
                                ArrayList<Room> list = dataSnapshot.getValue(genericTypeIndicator);
                                if(list == null) list = new ArrayList<>();

                                boolean flag;
                                do {
                                    flag = false;
                                    newId = createRandomString(5);
                                    for(Room r:list)
                                        if(r.getRoomId().equals(newId))
                                            flag = true;
                                }while (flag);

                                activity.room.setRoomId(newId);
                                activity.room.setPhase(0);
                                activity.room.setPlayerNum(np.getValue());
                                list.add(activity.room);
                                ref.setValue(list);


                                progressDialog.hide();
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment,new SetPositionFragment());
                                transaction.addToBackStack(null);
                                transaction.commit();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                databaseError.toException().printStackTrace();
                                System.out.println("error"+databaseError.getDetails());
                                progressDialog.hide();
                                Toast.makeText(getContext(),"エラーが発生しました。",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setNegativeButton("CANSEL", null)
                .setView(ll)
                .show();
    }

    static String createRandomString( int size ) {
        StringBuilder sb = new StringBuilder();
        for ( int i=0;i<size;i++ ) {
            sb.append( seed[(int)(seed.length*Math.random())] );
        }
        return sb.toString();
    }
    private static final char[] seed = new char[62];
    static {
        int i = 0;
        for ( char c = 'a'; c <= 'z'; c++ ) seed[i++] = c;
        for ( char c = '0'; c <= '9'; c++ ) seed[i++] = c;
        for ( char c = 'A'; c <= 'Z'; c++ ) seed[i++] = c;
    }
}