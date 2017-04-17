package net.mizucoffee.onenightwerewolf.welcome;

import android.app.ProgressDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableInt;
import android.databinding.adapters.SeekBarBindingAdapter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import net.mizucoffee.onenightwerewolf.Jinro;
import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.Room;
import net.mizucoffee.onenightwerewolf.databinding.FragmentSetPositionBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class SetPositionFragment extends Fragment {

    public final ObservableInt werewolf = new ObservableInt();
    public final ObservableInt seer = new ObservableInt();
    public final ObservableInt robber = new ObservableInt();
    public final ObservableInt minion = new ObservableInt();
    public final ObservableInt tanner = new ObservableInt();

    public final ObservableInt playerNum = new ObservableInt();

    FragmentSetPositionBinding mBinding;
    FirebaseDatabase mDatabase;
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
        return inflater.inflate(R.layout.fragment_set_position, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.bind(getView());
        mBinding.setPosition(this);
        activity.setSupportActionBar(mBinding.toolbar);
        mDatabase = FirebaseDatabase.getInstance();

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
        activity.setTitle( activity.getTitle() + " ID:" + activity.room.getRoomId() );

        playerNum.set(activity.room.getPlayerNum());

        switch (activity.room.getPlayerNum()) {
            case 3:
                werewolf.set(0);
                seer.set(1);
                robber.set(1);
                minion.set(0);
                tanner.set(0);
                break;
            case 4:
                werewolf.set(1);
                seer.set(1);
                robber.set(1);
                minion.set(1);
                tanner.set(0);
                break;
            case 5:
                werewolf.set(1);
                seer.set(1);
                robber.set(1);
                minion.set(1);
                tanner.set(1);
                break;
            case 6:
                werewolf.set(1);
                seer.set(2);
                robber.set(1);
                minion.set(1);
                tanner.set(1);
                break;
        }
    }

    public SeekBarBindingAdapter.OnProgressChanged progressChanged = new SeekBarBindingAdapter.OnProgressChanged() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int v = playerNum.get() -
                    (mBinding.werewolfSb.getProgress() +
                            mBinding.seerSb.getProgress() +
                            mBinding.robberSb.getProgress() +
                            mBinding.minionSb.getProgress() +
                            mBinding.tannerSb.getProgress() - 1);
            if (v < 0)
                seekBar.setProgress(seekBar.getProgress() + v);

            activity.room.setWerewolf(mBinding.werewolfSb.getProgress() + 1);
            activity.room.setSeerc(mBinding.seerSb.getProgress());
            activity.room.setRobber(mBinding.robberSb.getProgress());
            activity.room.setMinion(mBinding.minionSb.getProgress());
            activity.room.setTanner(mBinding.tannerSb.getProgress());
            activity.room.setVillager(playerNum.get() -
                    (mBinding.werewolfSb.getProgress() +
                            mBinding.seerSb.getProgress() +
                            mBinding.robberSb.getProgress() +
                            mBinding.minionSb.getProgress() +
                            mBinding.tannerSb.getProgress() + 1) + 2);

            activity.send();
        }
    };

    public void next(View v){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("読込中");
        progressDialog.setMessage("しばらくお待ち下さい...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();

        ArrayList<Integer> cards = new ArrayList<>();

        for(int i = 0;i <= 5; i++){
            switch (i){
                case 0:
                    for (int j = 0;j < (werewolf.get() + 1);j++)
                        cards.add(Jinro.WEREWOLF);
                    break;
                case 1:
                    for (int j = 0;j < seer.get();j++)
                        cards.add(Jinro.SEER);
                    break;
                case 2:
                    for (int j = 0;j < robber.get();j++)
                        cards.add(Jinro.ROBBER);
                    break;
                case 3:
                    for (int j = 0;j < minion.get();j++)
                        cards.add(Jinro.MINION);
                    break;
                case 4:
                    for (int j = 0;j < tanner.get();j++)
                        cards.add(Jinro.TANNER);
                    break;
                case 5:
                    for (int j = 0;j < (playerNum.get() -
                            (mBinding.werewolfSb.getProgress() +
                                    mBinding.seerSb.getProgress() +
                                    mBinding.robberSb.getProgress() +
                                    mBinding.minionSb.getProgress() +
                                    mBinding.tannerSb.getProgress() + 1) + 2 );j++)
                        cards.add(Jinro.VILLAGER);
                    break;
            }
        }

        activity.room.setWerewolf(mBinding.werewolfSb.getProgress() + 1);
        activity.room.setSeerc(mBinding.seerSb.getProgress());
        activity.room.setRobber(mBinding.robberSb.getProgress());
        activity.room.setMinion(mBinding.minionSb.getProgress());
        activity.room.setTanner(mBinding.tannerSb.getProgress());
        activity.room.setVillager(playerNum.get() -
                (mBinding.werewolfSb.getProgress() +
                        mBinding.seerSb.getProgress() +
                        mBinding.robberSb.getProgress() +
                        mBinding.minionSb.getProgress() +
                        mBinding.tannerSb.getProgress() + 1) + 2);

        activity.room.setCards(cards);

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("room");
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

                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment,new SetUsernameFragment());
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
}