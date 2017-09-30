package com.ongoza.itemsmenu.Utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;

import org.gearvrf.GVRBehavior;
import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;
import org.gearvrf.animation.GVRAnimation;
import org.gearvrf.animation.GVROnFinish;
import org.gearvrf.animation.GVROpacityAnimation;
import org.gearvrf.animation.GVRPositionAnimation;
import org.gearvrf.animation.GVRRepeatMode;
import org.gearvrf.script.GVRScriptBehavior;
import org.gearvrf.script.GVRScriptManager;

import static org.gearvrf.GVRRenderData.GVRRenderingOrder.OVERLAY;

/**
 * Created by os on 9/23/17.
 */

public class AlertMsg extends GVRSceneObject {
    private static final String TAG = MainActivity.getTAG();
    private GVRContext gContext;
    private int delay;
    private int lineTopPadding = 6;
    private int stroke = 4;

//    private
    private GVRSceneObject root;
    public AlertMsg(GVRContext gContext, int delay, String msg, int fontSize){
        super(gContext); this.gContext=gContext;
        Log.d(TAG,"Alert msg txt = "+msg);
        String[] arrMsg = msg.split("\n");
        int width = 0;
        Paint paint = new Paint();
        Rect r = new Rect();
        paint.setTextSize(fontSize);
        for(int i=0;i<arrMsg.length;i++){
            paint.getTextBounds(msg, 0, msg.length(), r);
            if(width<r.width()){width=r.width();}
        }
//        Log.d(TAG,"Alert msg size = "+width+"-"+arrMsg.length*r.height()*1.2f);
        showMsg2(arrMsg,true,width+4*stroke,arrMsg.length*(r.height()+lineTopPadding)+4*stroke,fontSize);

        this.delay = delay;
       // gContext.getMainScene().getMainCameraRig().addChildObject(root);
    }

    private void showMsg2(String[] str, boolean delete, int width, int height,int fontsize){
        Log.d(TAG,"Alert msg txt obj 1 "+width+" "+height);
        GVRTexture texture = createTextTexture(str,fontsize,width,height,-1);
        Log.d(TAG,"Alert msg txt obj 2");
        float w =( (float) width)/100, h = (height*1.2f)/100;
        root = new GVRSceneObject(gContext, w, h,texture);
        Log.d(TAG,"Alert msg txt obj 3");
        root.setName("alertMsg");
        root.getRenderData().setDepthTest(false);
        root.getTransform().setPosition(0, 0, -5f);
        root.getRenderData().setRenderingOrder(OVERLAY);
        root.getRenderData().getMaterial().setOpacity(0);
        Log.d(TAG,"Alert msg txt obj 4");
        gContext.getMainScene().getMainCameraRig().addChildObject(root);
        GVRAnimation an= new GVROpacityAnimation(root,2,1).setRepeatMode(GVRRepeatMode.ONCE).setRepeatCount(1).start(gContext.getAnimationEngine());
        if(delete){
            Log.d(TAG,"Alert msg txt obj 5");
            an.setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                Log.d(TAG,"Alert msg txt obj 6");
                delayDelete();}});
        }
    }

    private GVRTexture createTextTexture(String[] str, int fontSize, int width, int height, int fillColor){
        Rect r = new Rect();
        Bitmap bitmapAlpha = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmapAlpha);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        c.drawRoundRect(stroke,stroke,width-stroke,height-stroke,20,20,paint);
        paint.setStrokeWidth(stroke);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.STROKE);
        c.drawRoundRect(stroke,stroke,width-stroke,height-stroke,20,20,paint);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1F);
        paint.setTextSize(fontSize);
        for(int i=0;i<str.length;i++){
            if(str[i]!=null){
                paint.getTextBounds(str[i], 0, str[i].length(), r);
                float x = width/2f - r.width() / 2f - r.left;
                c.drawText(str[i],x,(fontSize+lineTopPadding)*(i+1),paint);
                Log.d(TAG,"Alert msg txt texture "+i+" "+x+" "+(fontSize+lineTopPadding)*i);
            }}
        if (fillColor>0){ c.drawColor(fillColor);}
        paint.setAlpha(255);
        return new GVRBitmapTexture(gContext,bitmapAlpha);
    }

    private void animateDeleteMsg(){
        Log.d(TAG,"delete Alert Message start delete");
//        new GVRPositionAnimation(root,1,0, 2f, -5f).setRepeatMode(GVRRepeatMode.ONCE).setRepeatCount(1).start(gContext.getAnimationEngine())
        new GVROpacityAnimation(root,1,0).setRepeatMode(GVRRepeatMode.ONCE).setRepeatCount(1).start(gContext.getAnimationEngine())
                .setOnFinish(new GVROnFinish(){ @Override public void finished(GVRAnimation animation){
                     Log.d(TAG,"delete 2 alert Message"+root.getName());
                    gContext.getMainScene().getMainCameraRig().removeChildObject(root);
                    delay=0; root = null;
                }});
    }

    private void addScript(GVRSceneObject item){
        // use with ScriptUtils java class
        GVRScriptManager sm = getGVRContext().getScriptManager();
        sm.addVariable("utils", new ScriptUtils());
        GVRScriptBehavior script = new GVRScriptBehavior(gContext);
        int delTime=300;
        script.setScriptText("importPackage(org.gearvrf); var sceneObject = null; var time="+delTime+";" +
                " function onInit(gvrf,sceneObj){sceneObject = sceneObj; utils.log(\"script log\");}" +
                " function onStep(){if (time>0){utils.log(\"script step \"+time); time--;}}"+
                "",GVRScriptManager.LANG_JAVASCRIPT);
        //       script.onStep();
        item.attachComponent(script);
    }

    private void delayDelete(){
        Log.d(TAG,"Alert msg delete timer");
        new java.util.Timer().schedule(new java.util.TimerTask(){
            @Override public void run() {
                        // your code here
                Log.d(TAG,"timer run");
                animateDeleteMsg();
            }}, delay);
    }
}
