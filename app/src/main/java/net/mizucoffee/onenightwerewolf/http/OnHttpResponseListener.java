package net.mizucoffee.onenightwerewolf.http;

/**
 * Created by KawakawaPlanning on 10/29/16.
 */
import java.util.EventListener;

public interface OnHttpResponseListener extends EventListener {
    void onResponse(String response);
}