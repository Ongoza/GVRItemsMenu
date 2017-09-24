package com.ongoza.itemsmenu.Utils;

import com.ongoza.itemsmenu.MainActivity;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;

/**
 * Created by os on 9/23/17.
 */

public class AlertMsg extends GVRSceneObject {
    private static final String TAG = MainActivity.getTAG();
    private GVRContext gContext;
    private GVRSceneObject root;
    public AlertMsg(GVRContext gContext, GVRSceneObject root, String msg){
        super(gContext); this.gContext=gContext; this.root=root;


    }
}
