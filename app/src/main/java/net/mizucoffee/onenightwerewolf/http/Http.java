package net.mizucoffee.onenightwerewolf.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by KawakawaPlanning on 10/29/16.
 */
public class Http {
    private OnHttpResponseListener resLis = null;
    public void get(final String host){
        Log.i("KPHTTP",host);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(host);
                    URLConnection uc = url.openConnection();
                    uc.setConnectTimeout(5000);

                    InputStream is = uc.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                    StringBuffer sb = new StringBuffer();
                    String s;
                    while ((s = reader.readLine()) != null) {
                        sb.append(s);
                    }



                    if (resLis != null)
                        resLis.onResponse(sb.toString());
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void setOnHttpResponseListener(OnHttpResponseListener listener){
        this.resLis = listener;
    }
    public void removeResponseListener(){
        this.resLis = null;
    }
}
