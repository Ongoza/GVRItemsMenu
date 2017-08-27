package com.ongoza.itemsmenu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.GVRTexture;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by os on 8/25/17.
 */

class TutorialItem extends GVRSceneObject {
    private GVRContext gContext;
    private static final String TAG = MainActivity.getTAG();
    private String BUTTON_TYPE_ITEM;
    GVRSceneObject root;
    boolean empty = true;
    private GVRTexture defaultTexture;
    private String BUTTON_TYPE_MENU;
    float[] sizeObj;
    JSONObject jsonObject;
    String number;

    TutorialItem(GVRContext gContext, int i, float[] size, float locx, float locy, GVRTexture texture, String cmd, String cmdItem) {
        super(gContext);
        this.gContext = gContext;
        this.number = String.valueOf(i); sizeObj = size; BUTTON_TYPE_MENU = cmd; BUTTON_TYPE_ITEM = cmdItem;
        defaultTexture = texture;
        jsonObject = new JSONObject();
//           Log.d(TAG,"start create item i="+cmd+"item"+cmdItem);
        empty = true;
        root = new GVRSceneObject(gContext,size[0],size[1],texture);
        root.setName("item_"+number); String[] tag={BUTTON_TYPE_MENU,BUTTON_TYPE_ITEM,number,""}; root.setTag(tag);
        root.getTransform().setPosition(locx,locy,0);
    }

    void addItem(JSONObject jObj){
        jsonObject = jObj; boolean ok=false;
//        Log.d(TAG,"Start add item "+jObj);
        String[] txt = new String[3];
        String id="";
        try{id = jsonObject.getString("_id"); txt[0] = jsonObject.getString("Name"); ok=true;
        }catch (Exception e){Log.d(TAG,"Add Item. Can not parse item object for tutorial "+jObj); txt[1]="Can not load data from server";}
        if(ok) {
            txt[1] = "No data";
            txt[2] = "No data";
            try {
                txt[1] = jsonObject.getString("Course");
            } catch (Exception e) {
                Log.d(TAG, "Add Item. Can not extract parameter \"Cource\" for tutorial "+id);
            }
            try {
                txt[2] = jsonObject.getString("Author");
            } catch (Exception e) {
                Log.d(TAG, "Add Item. Can not extract parameter \"Author\" for tutorial "+id);
            }
            int fillColor = TutorialMenu.EMPTY_ITEM_COLOR;
            try {
                fillColor = Color.parseColor(jsonObject.getString("backColor"));
            } catch (Exception e) {
                Log.d(TAG, "Add Item. Can not extract parameter \"backColor\" for tutorial "+id);
            }
            int fontColor = TutorialMenu.EMPTY_ITEM_COLOR;
            try {
                fontColor = Color.parseColor(jsonObject.getString("fontColor"));
            } catch (Exception e) {
                Log.d(TAG, "Add Item. Can not extract parameter \"fontColor\" for tutorial "+id);
            }
            int fontSize = 30;
            try {
                fontColor = Integer.valueOf(jsonObject.getString("fontSize"));
            } catch (Exception e) {
                Log.d(TAG, "Add Item. Can not extract parameter \"fontSize\" for tutorial "+id);
            }
            GVRTexture texture = createTextTexture(txt, fontSize, fontColor, fillColor);
            root.getRenderData().getMaterial().setMainTexture(texture);
            String[] tag = {BUTTON_TYPE_MENU, BUTTON_TYPE_ITEM, number, id};
            root.setTag(tag);
            root.attachCollider(new GVRSphereCollider(gContext));
            empty = false;
        }
//       Log.d(TAG,"end add item "+txt[0]);
    }

    void removeItem(){
        if(!empty){
            jsonObject = new JSONObject();
            root.detachCollider();
            String[] tag={BUTTON_TYPE_ITEM,number,""}; root.setTag(tag);
            root.getRenderData().getMaterial().setMainTexture(defaultTexture);
            empty = false;
        }
    }

    private GVRTexture createTextTexture(String[] str, int fontSize,int fontColor, int fillColor){
        Rect r = new Rect(); int width = (int) (sizeObj[0]*300), height= (int) (sizeObj[1]*300);
//        Log.d(TAG,"size w="+width+", h="+height);
        Bitmap bitmapAlpha = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        c.drawColor(fillColor);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
//        Log.d(TAG,"start bitmap fontsize="+fontSize+" fontColor="+fontColor+"fillColor"+fillColor);
//        fontColor = Color.argb(255,200,200,200);
        paint.setColor(fontColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1F);
        paint.setTextSize(fontSize);
        for(int i=0;i<str.length;i++){
            if(str[i]!=null){
                paint.getTextBounds(str[i], 0, str[i].length(), r);
                float starty= height/2f - fontSize*1.3f;
                float x = width/2f - r.width() / 2f - r.left;
//                if(i==str.length-1){paint.setTextSize(10); x=30;}
                c.drawText(str[i],x, starty+fontSize*1.3f*i,paint);
            }}
//        paint.setAlpha(255);
        return new GVRBitmapTexture(gContext,bitmapAlpha);
    }

}

