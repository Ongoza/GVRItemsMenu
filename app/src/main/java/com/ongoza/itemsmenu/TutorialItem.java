package com.ongoza.itemsmenu;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;

/**
 * Created by os on 8/25/17.
 */

class TutorialItem extends GVRSceneObject {
    GVRContext gContext;
    GVRSceneObject root;
    boolean empty = true;
    int id;

    TutorialItem(GVRContext gContext, int i, float[] size, float locx, float locy, GVRTexture texture) {
        super(gContext);
        this.gContext = gContext;
        this.id = i;
        empty = true;
        root = new GVRSceneObject(gContext,locx,locy,texture);
        root.getTransform().setPosition(locx,locy,0);
    }

    void addItem(String name){

    }

}

