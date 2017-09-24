package com.ongoza.itemsmenu.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;
import com.ongoza.itemsmenu.MainScene;
import com.ongoza.itemsmenu.Views.LoginMenu;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;

/**
 * Created by os on 9/19/17.
 */

public class KeyboardMain extends GVRSceneObject {
    private static final String TAG = MainActivity.getTAG();
    private final String KEY_TYPE = MainScene.KEY_TYPE;
    static final int KEY_COLOR_BACKGROUND =  Color.argb(255,35,35,35);
    static final int LABEL_COLOR_BACKGROUND =  Color.argb(255,88,211,247);
    static final int KEY_COLOR_TXT = Color.argb(255,255,250,240);
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
    OS_GUI os_gui;

    public KeyboardMain(GVRContext gContext) {
        super(gContext); this.gContext=gContext;
        root = new GVRSceneObject(gContext);
        root.getTransform().setPosition(0,0,-7f);
        os_gui = new OS_GUI();
        new LoginMenu(gContext,root,KEY_TYPE);
        os_gui.createItem(gContext,root, "label","","LoginLabel","Login",1.6f,inputSize[1],-3.5f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        curInput = os_gui.createItem(gContext,root, "label","","LoginInput","",inputSize[0],inputSize[1],0,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root, "button",KEY_TYPE,"LoginDel","<--",1.4f,inputSize[1],3.5f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root, "button",KEY_TYPE,"kbdOK","Login",1.3f,0.4f,2f,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root, "button",KEY_TYPE,"kbdCancel","Cancel",1.3f,0.4f,0f,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root, "button",KEY_TYPE,"kbdRegister","Register",1.3f,0.4f,-2f,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
    }
    public void onTouchEvent(String[] tag){
        switch (tag[1]) {
            case "LoginDel": delLastChar(); break;
            case "kbdOK": nextInput(false); break;
            case "kbdCancel":  cancelLogin(); break;
            case "kbdRegister":  nextInput(true); break;
            case "kbdKey": inputText = inputText+tag[2]; os_gui.updateText(curInput, inputText,inputSize);
                break;
            default: break;
        }
    }

    private void cancelLogin(){
        Log.d(TAG,"Cancel login");
        if(curInput.getName().equals("PasswordInput")){
            root.removeChildObject(gContext.getMainScene().getSceneObjectByName("PasswordLabel"));
            root.removeChildObject(curInput);
            //curInput = gContext.getMainScene().getSceneObjectByName("LoginLabel");
            curInput.getTransform().translate(0,-1,0);
            gContext.getMainScene().getSceneObjectByName("LoginLabel").getTransform().translate(0,-1,0);
        }else{
            Log.d(TAG,"Cancel Login");
            inputText = "";
            os_gui.updateText(curInput, inputText,inputSize);
        }
    }

    private void nextInput(boolean tr){
        if(curInput.getName().equals("LoginInput")){
            login = inputText; inputText="";
            curInput.getTransform().translate(0,1f,0);
            gContext.getMainScene().getSceneObjectByName("LoginLabel").getTransform().translate(0,1,0);
            curInput = os_gui.createItem(gContext,root, "label","","PasswordInput","",inputSize[0],inputSize[1],0,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
            os_gui.createItem(gContext,root, "label","","PasswordLabel","Password",1.6f,inputSize[1],-3.5f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));

        }else{
            password = inputText;
            if(tr){
                if(password.equals("")) {
                    Log.d(TAG, "End input data login=" + login + " password=" + password);
                    gContext.getMainScene().getSceneObjectByName("LoginInput").getTransform().translate(0, 1, 0);
                    gContext.getMainScene().getSceneObjectByName("LoginLabel").getTransform().translate(0, 1, 0);
                    gContext.getMainScene().getSceneObjectByName("PasswordLabel").getTransform().translate(0, 1, 0);
                    gContext.getMainScene().getSceneObjectByName("PasswordInput").getTransform().translate(0, 1, 0);
                    curInput = os_gui.createItem(gContext,root, "label","", "PasswordInput2", "", inputSize[0], inputSize[1], 0, KEYBOARD_TOP_PAD + 3 * (KEY_BUTTON_SIZE + KEY_BUTTON_PAD));
                    os_gui.createItem(gContext,root, "label","", "PasswordLabel2", "Password 2", 1.6f, inputSize[1], -3.5f, KEYBOARD_TOP_PAD + 3 * (KEY_BUTTON_SIZE + KEY_BUTTON_PAD));
                }else{
                    if(password.equals(inputText)){
                        //start register user

                    }else{
                        // show error message

                    }
                }
            }else {
                // check login and sync data
                Log.d(TAG,"Check login="+login+" password="+password);


            }
        }
    }

    private void delLastChar(){
        if(inputText.length()>0){
            inputText = inputText.substring(0,inputText.length()-2);
            os_gui.updateText(curInput, inputText,inputSize);
        }
    }



    public GVRSceneObject getRoot(){return  root;}




    private void saveLogin(String login, String pswd){
        SharedPreferences userdetails = gContext.getContext().getSharedPreferences("userDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userdetails.edit();
        editor.putString("login",login);
        editor.putString("pswd",pswd);
        editor.apply();
    }

//    private Bitmap createEmptyTexture(float sizeWidth, float sizeHeight){
//        int x = (int) (sizeWidth*300);
//        int y = (int) (sizeHeight*100);
//        int[] colors = new int[x*y];
//        Arrays.fill(colors, 0, x * y, LABEL_COLOR_BACKGROUND);
////                Arrays.fill(colors, 0, x * y, Color.argb(255,219,181,255));
//        Bitmap bitmapAlpha = Bitmap.createBitmap(colors, x, y, Bitmap.Config.ARGB_8888);
//        return bitmapAlpha;
//    }


//    private Bitmap createLabelTexture(float width, float height, String str){
//        Rect r = new Rect(); int w=10, h=(int) (height*100), fontSize = (int) (height*60);
////            Log.d(TAG,"label texture "+"height="+height+" fontSize="+fontSize);
//        if(width!=0){ w = (int) (width*100);
//        }else{
//            Paint testPaint = new Paint();
//            testPaint.setTextSize(h);
////                testPaint.setTextAlign(Paint.Align.CENTER);
//            w = (int) (testPaint.measureText(str)*10); // round
//        }
//        Bitmap bitmapAlpha = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bitmapAlpha);
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
//        c.drawColor(EMPTY_ITEM_COLOR);
//        paint.setColor(BUTTON_COLOR_TXT);
//        paint.setTextSize(fontSize);
//        paint.getTextBounds(str, 0, str.length(), r);
//        float x = w/2f - r.width() / 2f - r.left;
//        float y = h/2f - r.height() / 2f - r.top;
//        c.drawText(str,x,y,paint);
////            paint.setAlpha(255);
//        return bitmapAlpha;
//    }



}
