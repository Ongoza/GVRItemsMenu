package com.ongoza.itemsmenu.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;

import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;

/**
 * Created by os on 9/23/17.
 */

public class OS_GUI {
    private static final String TAG = MainActivity.getTAG();
    GVRContext gContext;
    GVRSceneObject root;
//    GVRSceneObject rootLocal;
    public GVRSceneObject createItem(GVRContext gContext,GVRSceneObject root, String type, String key, String name, String txt, float width, float height, float locx, float locy){
       this.gContext=gContext; this.root=root; GVRSceneObject item=new GVRSceneObject(gContext);
        switch (type){
            case "button":
                item = createLabel(true,key,name,txt,width,height,locx,locy);
                break;
            case "label":
                item = createLabel(false,key,name,txt,width,height,locx,locy);
                break;
            default: break;
        }
        return item;
    }

    private GVRSceneObject createLabel(boolean isButton, String key, String name, String txt, float width, float height, float locx, float locy){
            Bitmap bitmap = createButtonTexure(txt,width,height);
            Log.d(TAG,"createLabel="+width+" "+txt+" "+height);
            // if(width==0){width = bitmap.getWidth()/100;}
            GVRSceneObject item = new GVRSceneObject(gContext,width,height,new GVRBitmapTexture(gContext, bitmap));
            item.setName(name); String[] tag = {key,name}; item.setTag(tag);
            item.getTransform().setPosition(locx,locy,0.01f);
            if (isButton){item.attachComponent(new GVRSphereCollider(gContext));}
            root.addChildObject(item);
            return item;
        }
    private Bitmap createButtonTexure(String str, float width, float height){
        Log.d(TAG,"Pie 5");
        Rect r = new Rect();
        int w = (int) (width*100), h = (int) (height*100), fontSize = (int) (height*60);
        Log.d(TAG,"Pie ="+w+" "+h+" "+fontSize);
        Bitmap bitmapAlpha = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        //paint.setAntiAlias(true);
        float stroke = 5f;
        paint.setColor(Color.argb(250,146,220,248));
        paint.setStyle(Paint.Style.FILL);
        c.drawRoundRect(stroke,stroke,w-stroke,h-stroke,20,20,paint);
        //      c.drawRect(stroke,stroke,w,h,paint);
        //  c.drawRGB(200,200,200);

        paint.setStrokeWidth(stroke);
        paint.setColor(Color.argb(250,36,115,200));
        paint.setStyle(Paint.Style.STROKE);
        c.drawRoundRect(stroke,stroke,w-stroke,h-stroke,20,20,paint);
//
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.argb(250,10,10,10));
        paint.setStrokeWidth(2F);
        paint.setTextSize(fontSize);
        paint.getTextBounds(str, 0, str.length(), r);
        float x = w/2f - r.width() / 2f - r.left;
        c.drawText(str,x,h-10,paint);
//        c.drawColor(Color.argb(100,150,150,150));
//        c.drawColor(Color.argb(100,46,191,248));
        paint.setAlpha(255);
        return bitmapAlpha;
    }

    public void updateText(GVRSceneObject item,String newText, float[] inputSize){
        Bitmap bitmap = createButtonTexure(newText,inputSize[0],inputSize[1]);
        item.getRenderData().getMaterial().setMainTexture(new GVRBitmapTexture(gContext,bitmap));
    }

    }


