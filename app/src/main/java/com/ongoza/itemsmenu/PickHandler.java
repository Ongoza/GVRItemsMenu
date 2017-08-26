// todo сделать только один активный элемент - сейчас список и при пересечении объектов они все выделяются ;(

package com.ongoza.itemsmenu;

import android.graphics.Color;

import org.gearvrf.GVRPicker;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.IPickEvents;
import org.gearvrf.utility.Log;

/**
 * Created by os on 23.05.2017.
 */

public class PickHandler implements IPickEvents {
    private static final String TAG = MainActivity.getTAG();
    private String tutorialTag = "tMenu";
    private String sceneObjName=null;
//    private static final int IN_FOCUS_COLOR = 8570046;
    private static final int IN_FOCUS_COLOR =  Color.rgb(10,220,220);
//    private static final int LOST_FOCUS_COLOR = Main.getMenuBckClr();
//    private static final int LOST_FOCUS_COLOR = Main.getMsgBckClr();
//    private static final int CLICKED_COLOR = 12631476;
    public int startColor= 0;
    private long timer=0;
    public GVRSceneObject PickedObject = null;

    public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) {
//        Log.d(TAG,"info ="+pickInfo);
        try{if(sceneObj!=null){
//                Log.d(TAG,"focus="+String.valueOf(sceneObj.getName()));
                    if(PickedObject!=null){
                        Log.d(TAG,"check pick="+PickedObject.getName()+"="+sceneObjName);
                        if(!PickedObject.getName().equals(sceneObjName)){
                            Log.d(TAG,"check pick not equals ="+PickedObject.getName()+"="+sceneObjName);
                            PickedObject.getRenderData().getMaterial().setColor(startColor);
                    }}
                sceneObjName = sceneObj.getName();
                startColor = sceneObj.getRenderData().getMaterial().getRgbColor();
//                Log.d(Main.getTAG(),"pick obj color = "+startColor );
                sceneObj.getRenderData().getMaterial().setColor(IN_FOCUS_COLOR);
                PickedObject = sceneObj;
            }else{Log.e(TAG,"try pick empty obj");}
        }catch (Exception e){Log.d(TAG,"Error pick obj.");sceneObjName=null; PickedObject = null; }
    }

    public void onExit(GVRSceneObject sceneObj) {
        try{

//            String[] objTag = (String[]) sceneObj.getTag();
//            Log.d(TAG,"pick obj type = "+objTag[0] );
//        if(objTag[1].equals("mMenu")){
//            sceneObj.getRenderData().getMaterial().setColor(LOST_FOCUS_COLOR);
            sceneObj.getRenderData().getMaterial().setColor(startColor);
////            sceneObj.getTransform().translate(0,-0.1f,-0.2f);
//        }else{
//
//            GVRTextViewSceneObject objTxt = (GVRTextViewSceneObject) sceneObj;
//            objTxt.setBackgroundColor(Main.getMsgBckClr());
//        }
            sceneObjName=null; PickedObject = null;
    }catch (Exception e){PickedObject = null; Log.d(TAG,"Error exit pick obj.");}
    }

    public void onNoPick(GVRPicker picker) {
//        if(PickedObject!=null){
//            long curTime = System.currentTimeMillis();
//            if(timer>0) {
//                ColorTestScene.addTimerRes(PickedObject.getName(), curTime - timer);
//                timer=0;
//            }else{
//                long selTime = ColorTestScene.getTimer("startSelect");
//                ColorTestScene.addTimerRes(PickedObject.getName(), curTime - selTime);}
//
//            PickedObject.getTransform().setScale(1f,1f,1f);
//            PickedObject = null;
//        }else{
////            Log.d(TAG, "no Pick no name timer="+timer);
//            timer=0;}
    }

    public void onPick(GVRPicker picker) {
////        Log.d(TAG,"pick="+picker.getPicked()[0].getHitObject());
//        if(picker.getPicked().length>0){
//            GVRSceneObject sceneObj = picker.getPicked()[0].getHitObject();
//            if(sceneObj!=PickedObject){
//                if(PickedObject!=null){
//                    String name = PickedObject.getName();
//                    long curTime = System.currentTimeMillis();
////                    Log.d(TAG, "Pick no Pick name ="+name);
//                    if(timer>0) {
//                        ColorTestScene.addTimerRes(name, curTime - timer);
//                    }else{
//                        long selTime = ColorTestScene.getTimer("startSelect");
//                        ColorTestScene.addTimerRes(name, curTime - selTime);}
//                    PickedObject.getTransform().setScale(1f,1f,1f);
//                    PickedObject = null;}
//                timer = System.currentTimeMillis();
//                sceneObj.getTransform().setScale(1.2f,1.2f,1.2f);
//                PickedObject = sceneObj;
////            }else { Log.d(TAG,"Already picked ");
//            }
//        }
    }

    public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }

//    private void NonSelect(GVRSceneObject Picked) { }
}
