package com.ongoza.itemsmenu;

import android.content.Context;
import android.view.MotionEvent;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.GVRScene;
import org.gearvrf.utility.Log;

/**
 * Created by os on 8/25/17.
 */

public class Main extends GVRMain {
    private static final String TAG = MainActivity.getTAG();;
    public static Context mContext;
    public GVRContext gContext;
    private MainScene mainScene;
    private String curScene = "mainScene";

    public Main (MainActivity activity) {  mContext = activity; }

    @Override public void onInit(GVRContext gvrContext) throws Throwable {
        this.gContext = gvrContext;
        mainScene = new MainScene(gContext);
        Log.d(TAG,"start main");
        mainScene.show();
    }

    public void onTouchEvent(MotionEvent event) {
//        Log.d(TAG, "select event obj=" + String.valueOf(event.getAction()) + "=mpt=" + String.valueOf(MotionEvent.ACTION_MASK));
//        Log.d(TAG, "main="+String.valueOf(event. .getAction() & MotionEvent.ACTION_MASK)+" down="+String.valueOf(MotionEvent.ACTION_DOWN));
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                switch (curScene) {
                    case "mainScene":
//                        Log.w(TAG, "select event obj=" + String.valueOf(event.getAction()) + "=mpt=" + String.valueOf(MotionEvent.ACTION_MASK));
                        mainScene.onTouchEvent();
                        break;
                    default: break;
                }
            default: break;
        }
    }

}
