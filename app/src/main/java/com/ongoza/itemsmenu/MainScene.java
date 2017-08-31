package com.ongoza.itemsmenu;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRPicker;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.utility.Log;

public class MainScene extends GVRScene {
    private static final String TAG = MainActivity.getTAG();
    private static final String BUTTON_TUTORIAL_TYPE = "tListMenu";

    private GVRContext gContext;
    private GVRSceneObject root;
    private PickHandler mPickHandler;
    private GVRPicker mPicker;
    TutorialMenu tutorialMenu;


    public MainScene(GVRContext gContext) {
        super(gContext); this.gContext = gContext;
        Log.d(TAG,"create mainscena");
        getMainCameraRig().getOwnerObject().attachComponent(new GVRPicker(gContext, this));
        getMainCameraRig().getTransform().setPosition(0.0f, 0.0f, 0.0f);
//        getMainCameraRig().getTransform().setRotationByAxis(-90, 0, 1, 0);
        GVRSceneObject headTracker = new GVRSceneObject(gContext, gContext.createQuad(0.1f, 0.1f),
                gContext.getAssetLoader().loadTexture(new GVRAndroidResource(gContext, R.drawable.headtrackingpointer)));
        headTracker.getTransform().setPosition(0.0f, 0.0f, -3.0f);
        headTracker.setName("Head");
        headTracker.getRenderData().setDepthTest(false);
        headTracker.getRenderData().setRenderingOrder(100000);
        getMainCameraRig().addChildObject(headTracker);
        root = new GVRSceneObject(gContext);
        addSceneObject(root);
        mPickHandler = new PickHandler();
        getEventReceiver().addListener(mPickHandler);
        mPicker = new GVRPicker(gContext, this);
    }

    public void show(){
        Log.d(TAG,"show main scena");
        getGVRContext().setMainScene(this);
        tutorialMenu = new TutorialMenu(gContext,BUTTON_TUTORIAL_TYPE);

        tutorialMenu.show(root);
//        addSceneObject(menu);
    }
    public void onTouchEvent() {
        if (mPickHandler.PickedObject!=null){
//            Log.d(TAG, mPickHandler.PickedObject.getName()+"="+mPickHandler.PickedObject.getTag());
//            String name = mPickHandler.PickedObject.getName();
//            Log.d(TAG,"main scene touch obj = "+name);
            String[] arr = (String[]) mPickHandler.PickedObject.getTag();
//             Log.d(TAG,"main scene touch obj = "+arr[0]);
            switch (arr[0]) {
                case BUTTON_TUTORIAL_TYPE: tutorialMenu.onTouchEvent(arr);  break;
                default: break;
            }
        }
    }
}
