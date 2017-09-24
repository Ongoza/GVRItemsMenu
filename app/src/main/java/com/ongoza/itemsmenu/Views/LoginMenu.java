package com.ongoza.itemsmenu.Views;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.ongoza.itemsmenu.MainActivity;
import com.ongoza.itemsmenu.MainScene;

import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;

/**
 * Created by os on 9/23/17.
 */

public class LoginMenu extends GVRSceneObject{
    private static final String TAG = MainActivity.getTAG();
    private GVRContext gContext;
    private GVRSceneObject root;
    private String key_type;
    static final int KEY_COLOR_TXT = Color.argb(255,255,250,240);
    static final int KEY_COLOR_BACKGROUND =  Color.argb(255,35,35,35);
    static final float KEY_BUTTON_SIZE=0.5f;
    static final float KEY_BUTTON_PAD=0.25f;
    static final float KEYBOARD_TOP_PAD=1f;
    static final String[] KEYS = {"1234567890","qwertyuiop","asdfghjkl","zxcvbnm","_-#$.!%&*^+@"};

    public LoginMenu(GVRContext gContext, GVRSceneObject root, String key_type){
        super(gContext); this.gContext=gContext; this.root=root; this.key_type = key_type;
        createKeyboard();
    }

    private void createKeyboard(){
        for (int i=0;i<KEYS.length;i++){
            float left_pad=-KEYS[i].length()*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD)/2;
            for (int j=0;j<KEYS[i].length();j++){
                String name = Character.toString(KEYS[i].charAt(j));
                Bitmap bitmap = createKeyTexture(KEY_BUTTON_SIZE,KEY_BUTTON_SIZE,name);
                GVRSceneObject item = new GVRSceneObject(gContext,KEY_BUTTON_SIZE,KEY_BUTTON_SIZE,new GVRBitmapTexture(gContext, bitmap));
                item.setName(name); String[] tag = {key_type,"kbdKey",name}; item.setTag(tag);
                item.getTransform().setPosition(left_pad+j*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD),KEYBOARD_TOP_PAD-i*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD),0);
                item.attachComponent(new GVRSphereCollider(gContext));
                root.addChildObject(item);
            }
        }


    }

    private Bitmap createKeyTexture(float width, float height, String key){
        int w=(int) (width*100), h=(int) (height*100), fontSize = (int) (height*80);
        Bitmap bitmapAlpha = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        c.drawColor(KEY_COLOR_BACKGROUND);
        paint.setColor(KEY_COLOR_TXT);
        paint.setTextSize(fontSize);
        c.drawText(key,10,h-10,paint);
        return bitmapAlpha;
    }
}
