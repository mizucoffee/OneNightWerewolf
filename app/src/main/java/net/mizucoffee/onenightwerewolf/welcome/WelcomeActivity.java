package net.mizucoffee.onenightwerewolf.welcome;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import net.mizucoffee.onenightwerewolf.R;
import net.mizucoffee.onenightwerewolf.Room;

import java.util.ArrayList;

import static net.mizucoffee.onenightwerewolf.R.layout.activity;

public class WelcomeActivity extends AppCompatActivity {

    Room room;
    int nowServe = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment,new WelcomeFragment());
        transaction.commit();
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
}
