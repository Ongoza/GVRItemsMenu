package com.ongoza.itemsmenu.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;
import com.ongoza.itemsmenu.MainScene;

import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.GVRTexture;

import java.util.Arrays;

/**
 * Created by os on 9/19/17.
 */

public class KeyboardMain extends GVRSceneObject {
    private static final String TAG = MainActivity.getTAG();
    private final String KEY_TYPE = MainScene.KEY_TYPE;
    static final int KEY_COLOR_BACKGROUND =  Color.argb(255,35,35,35);
    static final int LABEL_COLOR_BACKGROUND =  Color.argb(255,88,211,247);
    static final int KEY_COLOR_TXT = Color.argb(255,255,250,240);
    static final String[] KEYS = {"1234567890","qwertyuiop","asdfghjkl","zxcvbnm","_-#$.!%&*^+@"};
    static final float KEY_BUTTON_SIZE=0.5f;
    static final float KEY_BUTTON_PAD=0.25f;
    static final float KEYBOARD_TOP_PAD=1f;
    static final int EMPTY_ITEM_COLOR =  Color.argb(255,35,35,35);
    static final int BUTTON_COLOR_TXT = Color.argb(255,255,250,240);
    static final float[] inputSize = {5f,0.5f};
//    static final float KEYBOARD_LEFT_PAD=-3.5f;
    String inputText = "";
    String login = "";
    String password = "";
    GVRSceneObject curInput;
    GVRSceneObject curInputLabel;
//    GVRSceneObject inputPassword;

    private GVRContext gContext;

    GVRSceneObject root;

    public KeyboardMain(GVRContext gContext) {
        super(gContext); this.gContext=gContext;
        root = new GVRSceneObject(gContext);
        root.getTransform().setPosition(0,0,-7f);
        createKeyboard();
        createLabel(root, false,"LoginLabel","Login",1.6f,inputSize[1],-3.5f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        curInput = createLabel(root, false,"LoginInput","",inputSize[0],inputSize[1],0,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        createLabel(root, true,"LoginDel","<--",1.4f,inputSize[1],3.5f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        createLabel(root, true,"kbdOK","OK",1.3f,0.4f,1f,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        createLabel(root, true,"kbdCancel","Cancel",1.3f,0.4f,-1f,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
    }
    public void onTouchEvent(String[] tag){
        switch (tag[1]) {
            case "LoginDel": delLastChar(); break;
            case "kbdOK": nextInput(); break;
            case "kbdCancel":  cancelLogin(); break;
            case "kbdKey": inputText = inputText+tag[2]; updateInputText(); break;
            default: break;
        }
    }

    private void cancelLogin(){
        Log.d(TAG,"Cancel start");
        if(curInput.getName().equals("PasswordInput")){
            root.removeChildObject(gContext.getMainScene().getSceneObjectByName("PasswordLabel"));
            root.removeChildObject(curInput);
            curInput = gContext.getMainScene().getSceneObjectByName("LoginLabel");
            curInput.getTransform().translate(0,-1,0);
            gContext.getMainScene().getSceneObjectByName("LoginLabel").getTransform().translate(0,-1,0);
        }else{
            Log.d(TAG,"Cancel Login");
        }
    }

    private void nextInput(){
        if(curInput.getName().equals("LoginInput")){
            login = inputText; inputText="";
            curInput.getTransform().translate(0,1f,0);
            gContext.getMainScene().getSceneObjectByName("LoginLabel").getTransform().translate(0,1,0);
            curInput = createLabel(root, false,"PasswordInput","",inputSize[0],inputSize[1],0,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
            createLabel(root, false,"PasswordLabel","Password",1.6f,inputSize[1],-3.5f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
            //Bitmap bitmap = createButtonTexure("Password",1.6f,inputSize[1]);
            //curInputLabel.getRenderData().getMaterial().setMainTexture(new GVRBitmapTexture(gContext,bitmap));
        }else{password = inputText;
            Log.d(TAG,"End input data login="+login+" password="+password);
        }
    }

    private void delLastChar(){
        if(inputText.length()>0){
            inputText = inputText.substring(0,inputText.length()-2);
            updateInputText();
        }
    }

    private void updateInputText(){
        Bitmap bitmap = createButtonTexure(inputText,inputSize[0],inputSize[1]);
        curInput.getRenderData().getMaterial().setMainTexture(new GVRBitmapTexture(gContext,bitmap));
    }

    public GVRSceneObject getRoot(){return  root;}

    private void createKeyboard(){
        for (int i=0;i<KEYS.length;i++){
            float left_pad=-KEYS[i].length()*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD)/2;
            for (int j=0;j<KEYS[i].length();j++){
                String name = Character.toString(KEYS[i].charAt(j));
                Bitmap bitmap = createKeyTexture(KEY_BUTTON_SIZE,KEY_BUTTON_SIZE,name);
                GVRSceneObject item = new GVRSceneObject(gContext,KEY_BUTTON_SIZE,KEY_BUTTON_SIZE,new GVRBitmapTexture(gContext, bitmap));
                item.setName(name); String[] tag = {KEY_TYPE,"kbdKey",name}; item.setTag(tag);
                item.getTransform().setPosition(left_pad+j*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD),KEYBOARD_TOP_PAD-i*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD),0);
                item.attachComponent(new GVRSphereCollider(gContext));
                root.addChildObject(item);
            }
        }


    }

    private GVRSceneObject createLabel(GVRSceneObject root, boolean isButton, String name, String txt, float width, float height, float locx, float locy){
        Bitmap bitmap = createButtonTexure(txt,width,height);
        Log.d(TAG,"createLabel="+width+" "+txt+" "+height);
       // if(width==0){width = bitmap.getWidth()/100;}
        GVRSceneObject item = new GVRSceneObject(gContext,width,height,new GVRBitmapTexture(gContext, bitmap));
        item.setName(name); String[] tag = {KEY_TYPE,name}; item.setTag(tag);
        item.getTransform().setPosition(locx,locy,0.01f);
        if (isButton){item.attachComponent(new GVRSphereCollider(gContext));}
        root.addChildObject(item);
        return item;
    }

    private Bitmap createEmptyTexture(float sizeWidth, float sizeHeight){
        int x = (int) (sizeWidth*300);
        int y = (int) (sizeHeight*100);
        int[] colors = new int[x*y];
        Arrays.fill(colors, 0, x * y, LABEL_COLOR_BACKGROUND);
//                Arrays.fill(colors, 0, x * y, Color.argb(255,219,181,255));
        Bitmap bitmapAlpha = Bitmap.createBitmap(colors, x, y, Bitmap.Config.ARGB_8888);
        return bitmapAlpha;
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

    private Bitmap createLabelTexture(float width, float height, String str){
        Rect r = new Rect(); int w=10, h=(int) (height*100), fontSize = (int) (height*60);
//            Log.d(TAG,"label texture "+"height="+height+" fontSize="+fontSize);
        if(width!=0){ w = (int) (width*100);
        }else{
            Paint testPaint = new Paint();
            testPaint.setTextSize(h);
//                testPaint.setTextAlign(Paint.Align.CENTER);
            w = (int) (testPaint.measureText(str)*10); // round
        }
        Bitmap bitmapAlpha = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        c.drawColor(EMPTY_ITEM_COLOR);
        paint.setColor(BUTTON_COLOR_TXT);
        paint.setTextSize(fontSize);
        paint.getTextBounds(str, 0, str.length(), r);
        float x = w/2f - r.width() / 2f - r.left;
        float y = h/2f - r.height() / 2f - r.top;
        c.drawText(str,x,y,paint);
//            paint.setAlpha(255);
        return bitmapAlpha;
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

}
