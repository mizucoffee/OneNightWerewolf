package net.mizucoffee.onenightwerewolf.old;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;

import net.mizucoffee.onenightwerewolf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class PollActivity extends AppCompatActivity {

    MyApplication app;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    Button[] btn;
    int player = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyApplication)getApplicationContext();
        Fabric.with(this, new Crashlytics());

        setContentView(R.layout.activity_poll);
        LinearLayout lll = (LinearLayout)findViewById(R.id.ll);
        player = getIntent().getIntExtra("card",0);
        btn = new Button[app.jinro.getPlayersNum()];
        if(app.id != null) setTitle( getTitle() + " ID:" + app.id );

        for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
            if(i == player) continue;
            btn[i] = new Button(this);
            btn[i].setText(app.jinro.playerNames.get(i));

            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            layoutParams1.setMargins(0, 0, 0, 48);

            btn[i].setLayoutParams(layoutParams1);
            btn[i].setPadding(0,16,0,16);
            btn[i].setBackgroundResource(R.drawable.next_btn_back);
            btn[i].setTextSize(24);
            btn[i].setOnClickListener(poll);
            ViewCompat.setElevation(btn[i],0);

            lll.addView(btn[i]);

        }

        ButterKnife.bind(this);
        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);

    }

    View.OnClickListener next = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setResult(1);
            finish();
        }
    };

    View.OnClickListener poll = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = 0;
            for(String s:app.jinro.playerNames) {
                if(s.equals(((Button)view).getText())){
                    break;
                }
                i++;
            }

            app.jinro.poll.add(i);

            setResult(1);
            finish();
        }
    };

}