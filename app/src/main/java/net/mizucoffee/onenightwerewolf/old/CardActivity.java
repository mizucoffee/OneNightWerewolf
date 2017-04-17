package net.mizucoffee.onenightwerewolf.old;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import net.mizucoffee.onenightwerewolf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class CardActivity extends AppCompatActivity {

    MyApplication app;
    @BindView(R.id.toolbar)
    Toolbar mToolBar;

    int card = 0;
    Button[] btn;
    int player = 0;
    Button nextBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = (MyApplication)getApplicationContext();
        Fabric.with(this, new Crashlytics());
        if(app.id != null) setTitle( getTitle() + " ID:" + app.id );

        player = getIntent().getIntExtra("card",0);

        System.out.println(app.jinro.cards.get(player));
        switch (app.jinro.cards.get(player)){
            case 0:
                setContentView(R.layout.activity_card_jinro);
                card = Jinro.WEREWOLF;
                if(app.werewolf == 2){
                    TextView tv = (TextView)findViewById(R.id.textView5);
                    for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
                        if(i == player) continue;
                        if(app.jinro.cards.get(i) == Jinro.WEREWOLF) {tv.setText(app.jinro.playerNames.get(i)+"さんも人狼です"); break;}
                    }
                }
                nextBtn = (Button)findViewById(R.id.nextBtn);
                nextBtn.setOnClickListener(next);
                break;
            case 1:
                setContentView(R.layout.activity_card_seer);
                LinearLayout ll = (LinearLayout)findViewById(R.id.ll);

                btn = new Button[app.jinro.getPlayersNum() + 1];

                for (int i = 0; i != app.jinro.getPlayersNum(); i++) {
                    if(i == player) continue;
                    btn[i] = new Button(this);
                    btn[i].setText(app.jinro.playerNames.get(i));

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                    layoutParams.setMargins(0, 0, 0, 48);

                    btn[i].setLayoutParams(layoutParams);
                    btn[i].setPadding(0,16,0,16);
                    btn[i].setBackgroundResource(R.drawable.next_btn_back);
                    btn[i].setTextSize(24);
                    btn[i].setOnClickListener(seer);
                    ViewCompat.setElevation(btn[i],0);

                    ll.addView(btn[i]);
                }

                btn[app.jinro.getPlayersNum()] = new Button(this);
                btn[app.jinro.getPlayersNum()].setText("場のカードを見る");

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                layoutParams.setMargins(0, 0, 0, 48);

                btn[app.jinro.getPlayersNum()].setLayoutParams(layoutParams);
                btn[app.jinro.getPlayersNum()].setPadding(0,16,0,16);
                btn[app.jinro.getPlayersNum()].setBackgroundResource(R.drawable.next_btn_back);
                btn[app.jinro.getPlayersNum()].setTextSize(24);
                btn[app.jinro.getPlayersNum()].setOnClickListener(seer);
                ViewCompat.setElevation(btn[app.jinro.getPlayersNum()],0);

                ll.addView(btn[app.jinro.getPlayersNum()]);


                card = Jinro.SEER;
                break;
            case 2:
                setContentView(R.layout.activity_card_robber);
                LinearLayout lll = (LinearLayout)findViewById(R.id.ll);

                btn = new Button[app.jinro.getPlayersNum()];

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
                    btn[i].setOnClickListener(robber);
                    ViewCompat.setElevation(btn[i],0);

                    lll.addView(btn[i]);

                }
                card = Jinro.ROBBER;
                break;
            case 3:
                setContentView(R.layout.activity_card_minion);
                card = Jinro.MINION;
                nextBtn = (Button)findViewById(R.id.nextBtn);
                nextBtn.setOnClickListener(next);
                break;
            case 4:
                setContentView(R.layout.activity_card_tanner);
                card = Jinro.TANNER;
                nextBtn = (Button)findViewById(R.id.nextBtn);
                nextBtn.setOnClickListener(next);
                break;
            case 5:
                setContentView(R.layout.activity_card_villager);
                card = Jinro.VILLAGER;
                nextBtn = (Button)findViewById(R.id.nextBtn);
                nextBtn.setOnClickListener(next);
                break;
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

    View.OnClickListener seer = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            System.out.println(((Button)view).getText());
            if(((Button)view).getText().equals("場のカードを見る")){
                app.jinro.seer.add(10);
                new AlertDialog.Builder(CardActivity.this)
                        .setTitle("占い結果")
                        .setMessage("場のカードは" + Jinro.getCardName(app.jinro.cards.get(app.jinro.getPlayersNum())) + "と" + Jinro.getCardName(app.jinro.cards.get(app.jinro.getPlayersNum()+1)) + "でした。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(1);
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }else{
                int i = 0;
                for(String s:app.jinro.playerNames) {
                    if(s.equals(((Button)view).getText())){
                        break;
                    }
                    i++;
                }
                app.jinro.seer.add(i);
                new AlertDialog.Builder(CardActivity.this)
                        .setTitle("占い結果")
                        .setMessage((((Button)view).getText())+"さんは" + Jinro.getCardName(app.jinro.cards.get(i)) + "でした。")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setResult(1);
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    };

    View.OnClickListener robber = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int i = 0;
            for(String s:app.jinro.playerNames) {
                if(s.equals(((Button)view).getText())){
                    break;
                }
                i++;
            }

            app.jinro.isSwap = true;
            app.jinro.swapPlayer = i;

            new AlertDialog.Builder(CardActivity.this)
                    .setTitle("入れ替え結果")
                    .setMessage((((Button)view).getText())+"さんと入れ替わりました。あなたは" + Jinro.getCardName(app.jinro.cards.get(i)) + "になりました。")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setResult(1);
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    };

}