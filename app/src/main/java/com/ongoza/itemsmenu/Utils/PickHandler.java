// todo сделать только один активный элемент - сейчас список и при пересечении объектов они все выделяются ;(

package com.ongoza.itemsmenu.Utils;

import android.graphics.Color;

import com.ongoza.itemsmenu.MainActivity;

import org.gearvrf.GVRPicker;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.IPickEvents;
import org.gearvrf.utility.Log;

/**
 * Created by os on 23.05.2017.
 */

public class PickHandler implements IPickEvents {
    private static final String TAG = MainActivity.getTAG();
    public GVRSceneObject PickedObject = null;

    public void onEnter(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) {
    }

    public void onExit(GVRSceneObject sceneObj) {
    }

    public void onNoPick(GVRPicker picker) {
        if(PickedObject!=null){
            PickedObject.getTransform().setScale(1f,1f,1f);
            PickedObject = null;
        }
    }

    public void onPick(GVRPicker picker) {
//        Log.d(TAG,"pick="+picker.getPicked()[0].getHitObject());
        if(picker.getPicked().length>0){
            GVRSceneObject sceneObj = picker.getPicked()[0].getHitObject();
            if(sceneObj!=PickedObject){
                if(PickedObject!=null){
//                    String name = PickedObject.getName();
//                  Log.d(TAG, "Pick no Pick name ="+name);
                    PickedObject.getTransform().setScale(1f,1f,1f);
                    PickedObject = null;}
                sceneObj.getTransform().setScale(1.2f,1.2f,1.2f);
                PickedObject = sceneObj;
            }
        }
    }

    public void onInside(GVRSceneObject sceneObj, GVRPicker.GVRPickedObject pickInfo) { }

//    private void NonSelect(GVRSceneObject Picked) { }
}
