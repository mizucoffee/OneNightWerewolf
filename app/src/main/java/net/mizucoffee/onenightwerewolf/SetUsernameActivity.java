package net.mizucoffee.onenightwerewolf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;

/**
 * Created by KawakawaPlanning2 on 2016/05/12.
 */
public class SetUsernameActivity extends AppCompatActivity{

    AppCompatEditText editText[];
    int playersNum;
    MyApplication app;

    @BindView(R.id.ll) LinearLayout ll;
    @BindView(R.id.root) LinearLayout root;
    @BindView(R.id.toolbar) Toolbar mToolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //全てのアクティビティですべき処理 ↓
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_username);
        app = (MyApplication)getApplicationContext();
        ButterKnife.bind(this);
        Fabric.with(this, new Crashlytics());

        setSupportActionBar(mToolBar);
        app.setTitlebarFont(mToolBar);
        //全てのアクティビティですべき処理 ↑

        playersNum = app.jinro.getPlayersNum();

        editText = new AppCompatEditText[playersNum];

        for (int i = 0; i != playersNum; i++) {
            editText[i] = new AppCompatEditText(this);
            LinearLayout l = new LinearLayout(this);
            TextView t = new TextView(this);
            t.setPadding(0,0,16,0);
            t.setText("Player "+ (i + 1) );
            editText[i].setHint("Player "+ (i + 1));
            editText[i].setInputType(InputType.TYPE_CLASS_TEXT);
            l.addView(t,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            l.addView(editText[i],new LinearLayout.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            ll.addView(l);
        }
    }

    @OnClick(R.id.nextBtn)
    public void next(){
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
            if (s.indexOf("場の") != -1)
                flag = false;
        }

        if(!overlapCheck(l)){
            if(flag) {
                for (EditText et : editText) {
                    if (et.getText().toString().equals("")) {
                        app.jinro.addPlayer(et.getHint().toString());
                    } else {
                        app.jinro.addPlayer(et.getText().toString());
                    }
                }

                Intent intent = new Intent();
                intent.setClass(this, CardServeActivity.class);
                startActivity(intent);
                finish();
            }else{
                Snackbar snackbar = Snackbar.make(root, "使用できない文字列が含まれています。変更してください。", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }else{
            Snackbar snackbar = Snackbar.make(root, "名前が重複しています。変更してください。", Snackbar.LENGTH_SHORT);
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