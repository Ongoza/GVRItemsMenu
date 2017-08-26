package com.ongoza.itemsmenu;

import android.os.Bundle;
import android.view.MotionEvent;

import org.gearvrf.GVRActivity;
import org.gearvrf.utility.Log;

/**
 * Created by os on 8/25/17.
 */

public class MainActivity extends GVRActivity {
    //    private static final int BUTTON_INTERVAL = 500;
//    private static final int TAP_INTERVAL = 300;
//    private long mLatestButton = 0;
//    private long mLatestTap = 0;
    private static final String TAG = "ItemsMenu";
    Main main = null;


    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"start activity");
        main = new Main(this);
        setMain(main);
    }

    @Override public boolean onTouchEvent(MotionEvent event) {
//        Log.d(TAG, " motion event");
        main.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    public static String getTAG(){ return TAG; }


//    @Override public void onBackPressed() {
//        if (System.currentTimeMillis() > mLatestButton + BUTTON_INTERVAL) {
//            mLatestButton = System.currentTimeMillis();
//        }
//    }
//    @Override public boolean onKeyLongPress(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            mLatestButton = System.currentTimeMillis();
//        }
//        return super.onKeyLongPress(keyCode, event);
//    }

    //@Override
//    public boolean onSingleTap(MotionEvent e) {
////        Log.d(TAG, "onSingleTap");
//        if (System.currentTimeMillis() > mLatestTap + TAP_INTERVAL) {
//            mLatestTap = System.currentTimeMillis();
////            main.onSingleTap(e);
//        }
//        return false;
//    }


}
