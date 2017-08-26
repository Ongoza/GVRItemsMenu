package com.ongoza.itemsmenu;

import android.content.Context;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRScene;
//import android.R;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.utility.Log;
//import com.ongoza.itemsmenu.R;
/**
 * Created by os on 8/25/17.
 */

public class MainScene extends GVRScene {
    private static final String TAG = MainActivity.getTAG();
    private GVRContext gContext;

    private PickHandler mPickHandler;
    private GVRPicker mPicker;


    public MainScene(GVRContext gContext) {
        super(gContext); this.gContext = gContext;
        Log.d(TAG,"create mainscena");
        getMainCameraRig().getOwnerObject().attachComponent(new GVRPicker(gContext, this));
        getMainCameraRig().getTransform().setPosition(0.0f, 0.0f, 0.0f);
        getMainCameraRig().getTransform().setRotationByAxis(-90, 0, 1, 0);
        GVRSceneObject headTracker = new GVRSceneObject(gContext, gContext.createQuad(0.1f, 0.1f),
                gContext.getAssetLoader().loadTexture(new GVRAndroidResource(gContext, R.drawable.headtrackingpointer)));
        headTracker.getTransform().setPosition(0.0f, 0.0f, -1.0f);
        headTracker.setName("Head");
        headTracker.getRenderData().setDepthTest(false);
        headTracker.getRenderData().setRenderingOrder(100000);
//        getMainCameraRig().addChildObject(headTracker);
        mPickHandler = new PickHandler();
        getEventReceiver().addListener(mPickHandler);
        mPicker = new GVRPicker(gContext, this);
    }

    public void show(){
        Log.d(TAG,"show main scena");
        getGVRContext().setMainScene(this);
        TutorialMenu menu = new TutorialMenu(gContext);
        addSceneObject(menu);
    }
    public void onTouchEvent() {
        if (mPickHandler.PickedObject!=null){
//            Log.d(TAG, mPickHandler.PickedObject.getName()+"="+mPickHandler.PickedObject.getTag());
            String[] arr = (String[]) mPickHandler.PickedObject.getTag();
            String name = mPickHandler.PickedObject.getName();
//            String selScene= name.substring(0,4);
            Log.d(TAG,"main scene tag = "+arr[0]+" name="+name+" SelScene="+arr[1]);
            switch (arr[0]) {
                case "tMenu":
                    switch(arr[1]){
                        case "video":
//                            hide();
                            break;
                        default: break;
                    }
                    break;
                case "mMenu":
                    switch(name){
                        case "aboutOk":
//                            shortcutMenu.hideAbout();
                            break;
                        case "about":
//                            shortcutMenu.showAbout();
                            break;
                        case "restart": break;
                        //                  case "home": break;

                        default: break;
                    } break;
                default: break;
            }
        }
    }
}
