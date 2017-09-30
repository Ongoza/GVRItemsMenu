package com.ongoza.itemsmenu.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;
import com.ongoza.itemsmenu.MainScene;
import com.ongoza.itemsmenu.Views.LoginMenu;
import com.ongoza.itemsmenu.Views.ConnectionManager;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;

import java.util.List;

/**
 * Created by os on 9/19/17.
 */

public class LoginForm extends GVRSceneObject {
    private static final String TAG = MainActivity.getTAG();
    private final String KEY_TYPE = MainScene.KEY_TYPE;
    private static final int KEY_COLOR_BACKGROUND =  Color.argb(255,35,35,35);
    private static final int LABEL_COLOR_BACKGROUND =  Color.argb(255,88,211,247);
    private static final int KEY_COLOR_TXT = Color.argb(255,255,250,240);
    private static final float KEY_BUTTON_SIZE=0.5f;
    private static final float KEY_BUTTON_PAD=0.25f;
    private static final float KEYBOARD_TOP_PAD=1f;
    private static final int EMPTY_ITEM_COLOR =  Color.argb(255,35,35,35);
    private static final int BUTTON_COLOR_TXT = Color.argb(255,255,250,240);
    private static final float[] LABEL_SIZE = {6f,0.6f};
    private static final float[] BUTTON_SIZE = {1.3f,0.4f};
//    static final float KEYBOARD_LEFT_PAD=-3.5f;
    private String inputText;
    private String login;
    private String password;
    private boolean registerNew;
    private GVRSceneObject curInput;
    private GVRSceneObject curInputLabel;
    private ConnectionManager connectionManager;
//    GVRSceneObject inputPassword;

    private GVRContext gContext;
    private GVRSceneObject root;
    private GVRSceneObject root_form;
    private OS_GUI os_gui;

    public LoginForm(GVRContext gContext) {
        super(gContext); this.gContext=gContext;
        root = new GVRSceneObject(gContext);
        root.getTransform().setPosition(0,0,-7f);
        os_gui = new OS_GUI();
        new LoginMenu(gContext,root,KEY_TYPE);
        startNew();
    }

    private void startNew(){
        login="";password=""; inputText=""; registerNew = false;
        root_form = new GVRSceneObject(gContext);
        os_gui.createItem(gContext,root_form, "label","","LoginLabel","Login",1.6f,LABEL_SIZE[1],-3.9f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        curInput = os_gui.createItem(gContext,root_form, "label","","LoginInput","",LABEL_SIZE[0],LABEL_SIZE[1],0,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root_form, "button",KEY_TYPE,"LoginDel","<--",1.4f,LABEL_SIZE[1],3.9f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root_form, "button",KEY_TYPE,"kbdOK","OK",BUTTON_SIZE[0],BUTTON_SIZE[1],2f,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root_form, "button",KEY_TYPE,"kbdCancel","Cancel",BUTTON_SIZE[0],BUTTON_SIZE[1],0,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        os_gui.createItem(gContext,root_form, "button",KEY_TYPE,"kbdRegister","New user",BUTTON_SIZE[0],BUTTON_SIZE[1],-2f,KEYBOARD_TOP_PAD +2*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        root.addChildObject(root_form);
    }

    public void onTouchEvent(String[] tag){
        switch (tag[1]) {
            case "LoginDel": delLastChar(); break;
            case "kbdOK": nextInput(); break;
            case "kbdCancel":  cancelLogin(); break;
            case "kbdRegister":  nextRegister(); break;
            case "kbdKey": inputText = inputText+tag[2]; os_gui.updateText(curInput, inputText,LABEL_SIZE);
                break;
            default: break;
        }
    }

    private void cancelLogin(){
        Log.d(TAG,"Cancel login");
        root.removeChildObject(root_form);
//        for (int i = 0; i < list.size(); i++) {
//            root.removeChildObject(list.get(i));
//        }
        startNew();
    }

    public void show(GVRSceneObject root, ConnectionManager connectionManager){
        root.addChildObject(this.getRoot());
        this.connectionManager = connectionManager;
    }
    
    private void nextRegister(){
        if(registerNew){
            registerNew=false;
            os_gui.updateText(gContext.getMainScene().getSceneObjectByName("kbdRegister"), "New user",BUTTON_SIZE);
        }else{
            registerNew=true;
            os_gui.updateText(gContext.getMainScene().getSceneObjectByName("kbdRegister"), "Creating",BUTTON_SIZE);
        }
    }

    private void nextInput(){
        if(curInput.getName().equals("LoginInput")){
            login = inputText; inputText="";
            curInput.getTransform().translate(0,1f,0);
            gContext.getMainScene().getSceneObjectByName("LoginLabel").getTransform().translate(0,1,0);
            curInput = os_gui.createItem(gContext,root_form, "label","","PasswordInput","",LABEL_SIZE[0],LABEL_SIZE[1],0,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
            os_gui.createItem(gContext,root_form, "label","","PasswordLabel","Password",1.7f,LABEL_SIZE[1],-3.9f,KEYBOARD_TOP_PAD +3*(KEY_BUTTON_SIZE+KEY_BUTTON_PAD));
        }else{
            Log.d(TAG, "End input data login tr=" +registerNew);
            if(registerNew){
                if(password.equals("")) {
                    Log.d(TAG, "End input data login=" + login + " password=" + password);
                    inputText="";
                    gContext.getMainScene().getSceneObjectByName("LoginInput").getTransform().translate(0, 1, 0);
                    gContext.getMainScene().getSceneObjectByName("LoginLabel").getTransform().translate(0, 1, 0);
                    gContext.getMainScene().getSceneObjectByName("PasswordLabel").getTransform().translate(0, 1, 0);
                    gContext.getMainScene().getSceneObjectByName("PasswordInput").getTransform().translate(0, 1, 0);
                    curInput = os_gui.createItem(gContext,root_form, "label","", "PasswordInput2", "", LABEL_SIZE[0], LABEL_SIZE[1], 0, KEYBOARD_TOP_PAD + 3 * (KEY_BUTTON_SIZE + KEY_BUTTON_PAD));
                    os_gui.createItem(gContext,root_form, "label","", "PasswordLabel2", "Password 2", 1.6f, LABEL_SIZE[1], -3.9f, KEYBOARD_TOP_PAD + 3 * (KEY_BUTTON_SIZE + KEY_BUTTON_PAD));
                }else{
                    if(password.equals(inputText)){
                        //start register user
                        password = inputText;
                        String query="?l="+login+"&p="+password;
                        boolean sended= connectionManager.startSendCmdToServer("register",query);
                        if(!sended){new AlertMsg(gContext,3000,"Attention!\n Can not connect to server.\nPlease check Internet connection.",24);}
                    }else{new AlertMsg(gContext,3000,"Attention!\n Your new password and confirmation password do not match.",24);}
                }
            }else {
                // check login and sync data
                password = inputText;
                String query="?l="+login+"&p="+password;
                Log.d(TAG,"Check login="+login+" password="+password);
                boolean sended= connectionManager.startSendCmdToServer("login",query);
                if(!sended){new AlertMsg(gContext,3000,"Attention!\n Can not connect to server.\nPlease check Internet connection.",24);}
            }
        }
    }

    private void delLastChar(){
        if(inputText.length()>0){
            inputText = inputText.substring(0,inputText.length()-2);
            os_gui.updateText(curInput, inputText,LABEL_SIZE);
        }
    }

    public GVRSceneObject getRoot(){return  root;}

    private void hide(){
//        for (GVRSceneObject object : getWholeSceneObjects()) {
//            if (object.getRenderData() != null && object.getRenderData().getMaterial() != null) {
//                object.getRenderData().getMaterial().setOpacity(0f);
//            }
//        }
    }
    

}
