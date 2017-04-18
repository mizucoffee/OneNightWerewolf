package net.mizucoffee.onenightwerewolf.jinro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.Room;

import java.util.ArrayList;
import java.util.Collections;

import static net.mizucoffee.onenightwerewolf.R.layout.activity;

public class JinroActivity extends AppCompatActivity {

    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity);

        room = new Gson().fromJson(getIntent().getStringExtra("room"),Room.class);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment,new CheckNameFragment());
        transaction.commit();

        setTitle( getTitle() + " ID:" + room.getRoomId() );

        keyEventTimer = new CountDownTimer(1000, 100) {
            @Override public void onTick(long millisUntilFinished) {}
            @Override public void onFinish() {
                pressed = false;
            }
        };

        refresh();
    }

    public void refresh(){
        ArrayList c = room.getCards();
        Collections.shuffle(c);
        room.setCards(c);

        room.setSeer(null);
        room.setSwap(null);
        room.setPoll(null);

        send();
    }

    public void send(){
        FirebaseDatabase.getInstance().getReference("room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<ArrayList<Room>> genericTypeIndicator = new GenericTypeIndicator<ArrayList<Room>>() {};
                ArrayList<Room> list = dataSnapshot.getValue(genericTypeIndicator);
                if(list == null) list = new ArrayList<>();
                for(int i = 0;i != list.size();i++)
                    if(list.get(i).getRoomId().equals(room.getRoomId()))
                        list.set(i,room);

                FirebaseDatabase.getInstance().getReference("room").setValue(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
                System.out.println("error"+databaseError.getDetails());
            }
        });
    }

    private CountDownTimer keyEventTimer;
    private boolean pressed = false;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
            if(!pressed) {
                keyEventTimer.cancel();
                keyEventTimer.start();
                Toast.makeText(this, "終了する場合は、もう一度バックボタンを押してください", Toast.LENGTH_SHORT).show();
                pressed = true;
                return false;
            }
        return super.dispatchKeyEvent(event);
    }
}
