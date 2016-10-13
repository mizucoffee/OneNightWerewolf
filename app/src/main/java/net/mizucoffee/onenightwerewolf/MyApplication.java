package net.mizucoffee.onenightwerewolf;

import android.app.Application;
import android.graphics.Typeface;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.lang.reflect.Field;

/**
 * Created by KawakawaPlanning on 10/8/16.
 */
public class MyApplication extends Application {

    public Jinro jinro;
    private Typeface mMplusLight;

    public static String APPLICATION_ID = "A6B7B220";

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        jinro = new Jinro();
        mMplusLight = Typeface.createFromAsset(getAssets(), "mplus-1c-light.ttf");
    }

    public void resetJinro(){
        jinro = new Jinro();
    }

    public void setTitlebarFont(Toolbar toolbar) {
        TextView titleTextView = null;

        try {
            Field f = toolbar.getClass().getDeclaredField("mTitleTextView");
            f.setAccessible(true);
            titleTextView = (TextView) f.get(toolbar);
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        titleTextView.setTypeface(mMplusLight);
    }
}
