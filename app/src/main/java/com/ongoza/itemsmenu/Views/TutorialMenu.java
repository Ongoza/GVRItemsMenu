package com.ongoza.itemsmenu.Views;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;
import com.ongoza.itemsmenu.Utils.AlertMsg;
import com.ongoza.itemsmenu.Utils.LoginForm;

import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.GVRTexture;
import org.gearvrf.scene_objects.GVRTextViewSceneObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Arrays;

import static java.lang.Math.atan;
import static java.lang.Math.toDegrees;

/**
 * Created by os on 8/25/17.
 */

public class TutorialMenu extends GVRSceneObject {
    private String BUTTON_TYPE;
    private final String BUTTON_TYPE_ITEM = "tutItem";
    static final int BUTTON_COLOR_TXT = Color.argb(255,255,250,240);
    private final int ITEMS_PER_COLUMN = 3;
    private final float ITEMS_PAD_COLUMN = 0.5f;
    private final String DEFAULT_MENU_TEXT = "This menu allows you select one from existing tutorials.";
    private final int ITEMS_PER_ROW = 3;
    private final float ITEMS_PAD_ROW = 0.5f;
    private final float[] ITEM_SIZE = new float[]{1.5f,1f};
    static final int EMPTY_ITEM_COLOR =  Color.argb(255,35,35,35);
    private final String[] detailsText= {"Name","Course","Author","Price","Size","Tags"};
//    private final int EMPTY_ITEM_COLOR =  Color.argb(255,135,135,125);
    private final float FOTTER_HEIGHT = 0.3f;
    GVRContext gContext;
    GVRSceneObject root;
    private static final String TAG = MainActivity.getTAG();
    JSONArray tutorialsArray = new JSONArray();
    int curPage = 1;
    int totalPages = 1;
    private ConnectionManager connectionManager;
    GVRSceneObject mainRoot;
    TutorialMenu tMenu;
    int selectedTutorialNumber = -1;
    int itemsPerPage = ITEMS_PER_COLUMN*ITEMS_PER_ROW;
    TutorialItem[] tutorialItems = new TutorialItem[itemsPerPage];
    LoginForm loginForm;
    String userLogin, userPass;

    public interface DownloadListener {
        void onSuccess();
        void onFailure();
    }

    public  TutorialMenu(GVRContext gContext, String btnType) {
            super(gContext);
            this.gContext = gContext;
            this.tMenu = this;
            BUTTON_TYPE = btnType;
            Log.d(TAG,"start menu");
            connectionManager = new ConnectionManager(this,gContext);
            root = new GVRSceneObject(gContext);
            root.getTransform().setPosition(0,0,-6.5f);
        }

    private void checkLocalDir(){
            Log.d(TAG,"check local files=");
            try{
                File mydir = gContext.getContext().getFilesDir();
                File listFile[] = mydir.listFiles();
                Log.d(TAG,"check local files="+Arrays.toString(listFile));
            if (listFile != null && listFile.length > 0) {
                for (File aListFile : listFile) {
                    Log.d(TAG,"file="+aListFile.getName());
                }
            }

                File file = gContext.getContext().getFileStreamPath("test1.mp4");
                if (file.exists()){
                    Log.d(TAG,"file test4 exist!!");
                }
//
            }catch(Exception e){
                    Log.d(TAG, "Can not connected to  server with video "+ Arrays.toString(e.getStackTrace()));
                }
            }

    private void showItems(){
//            Log.d(TAG,"start show items for current page "+curPage);
            hideItemDetails();
            totalPages = tutorialsArray.length()/itemsPerPage+1;
            for(int j=0, i=(curPage-1)*itemsPerPage;j<itemsPerPage;i++,j++){
                tutorialItems[j].removeItem();
                if(i<tutorialsArray.length()){
                    try{JSONObject curItem = tutorialsArray.getJSONObject(i);
                        tutorialItems[j].addItem(curItem);
                    }catch (Exception e){Log.d(TAG,"Tutorial menu. Can not parse json object "+i+" menu="+j);}
                }}
            updateLabel("pagesList",String.valueOf(curPage)+"/"+String.valueOf(totalPages));
            }

    public void show(GVRSceneObject main, LoginForm loginForm){
        Log.d(TAG,"start show tutorial menu ");
        this.mainRoot = main;
        this.loginForm = loginForm;
        main.addChildObject(root);
        SharedPreferences userdetails = gContext.getContext().getSharedPreferences("com.ongoza.VRTE4.userDetails", Context.MODE_PRIVATE);
        userLogin = userdetails.getString("login", "");
        userPass = userdetails.getString("pswd", "");
        Log.d(TAG,"saved login "+userLogin+" "+userPass);
        String msg = "Alert message!\n Start login to server.....\nOk";
        new AlertMsg(gContext,5000,msg,24);
//        if(!userLogin.equals("")&&!userPass.equals("")){loginToServer();
//        }else{// no saved data: login or create account
     //       loginForm.show(root,connectionManager);
    //}
        }

    public void hide(){ mainRoot.removeChildObject(root); }

    private void loadItemsServer(){
        //String allData = loadItemsLocal();
//        _find?criteria=%7B%22x%22%3A2%7D'
        // _more?id=1&amp;batch_size=1'
        // find?batch_size=1'
        String data = "?batch_size="+Integer.toString(itemsPerPage);

        //final ResultListener listener
        connectionManager.startDownload("takeAllTutorials","AllTutorials", data);
    }

    public void updateTutorials(JSONArray jArr){
       tutorialsArray = jArr; showItems();
    }

    public void loginToServer(){
            // check login&&pass
            Log.d(TAG,"login&pswd="+userLogin+" "+userPass);
            String data='?'+userLogin+'&'+userPass;
        String type = "login";
            //final ResultListener listener
            boolean sended= connectionManager.startSendCmdToServer(type,data);
            Log.d(TAG,"Result send to server: "+sended);
            //checkLogin(uname,upass);
            // sync tutorial data
        if(sended){
            root.removeChildObject(loginForm.getRoot());
            //TODO show message: wait for the server answer
        }else{
            //TODO show message: check connection to Inet
        }
    }

    private void startShowMainMenu(){
        createHeader();
        createFooter();
        createLeftPanel();
        createRightPanel();
//            loadItemsLocal();
        loadItemsServer();
        createEmptyItems();
        showItems();
    }

    public void resultServerCommand(String cmd, String query,String result){
        Log.d(TAG, "Server cmd "+cmd+query+" Result: "+result);
    }

    public void saveLogin(String login, String pswd){
        SharedPreferences local_data = gContext.getContext().getSharedPreferences("com.ongoza.VRTE4.userDetails",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = local_data.edit();
        editor.putString("login",login);
        editor.putString("pswd",pswd);
        editor.apply();
    }

    public void showMainMenu(){
        checkLocalDir();
    }

    private String loadItemsLocal(){
        String allData ="";
        int len = 20;
            for(int i=0; i<len;i++){
                String jStr=
                        "{\"_id\":\"BAAAAB"+i+"\""
                                +",\"Name\":\"Tutorial "+i+"\""
                                +",\"backColor\":\"#222222\""
                                +",\"fontSze\":\"30\""
                                +",\"fontColor\":\"#FFFFFF\""
                                +",\"Author\":\"Oleg Skuibida\""
                                +",\"Tags\":[\"Common\",\" Cooking\",\" Amateur\",\" Home\"]"
                                +",\"Course\":\"Common\""
                                +",\"Price\":\"Free\""
                                +",\"Size\":\"10 MB\""
                                +"}";
                allData=allData+jStr;  if(i<(len-1)){allData=allData+","; }
                try {JSONObject item = new JSONObject(jStr); tutorialsArray.put(item);
                }catch (Exception e){ Log.d(TAG,"Error parse json");
                }
            }
        //    Log.d(TAG,"tutorialsArray ="+allData);
        return allData;
        }

    private void createEmptyItems(){
            Bitmap bitmapTextureEmpty = createEmptyTexture(ITEM_SIZE[0],ITEM_SIZE[1]);
            float startX, startY,stepX=ITEMS_PAD_ROW+ITEM_SIZE[0], stepY=ITEMS_PAD_COLUMN+ITEM_SIZE[1];
            float x= (float) ITEMS_PER_ROW ; float y= (float) ITEMS_PER_COLUMN;
            float middleX = x/2, middleXFloor = ITEMS_PER_ROW/2;
            float middleY = y/2, middleYFloor = ITEMS_PER_COLUMN/2;
//            Log.d(TAG,"middleX =="+middleX+" "+middleXFloor+" "+x);
            if(middleX>middleXFloor){startX = -middleXFloor*stepX;
            }else{startX = -stepX/2-(middleXFloor-1)*stepX;}
            if(middleY>middleYFloor){startY = middleYFloor*stepY;
            }else{startY = stepY/2+(middleYFloor-1)*stepY;}
            GVRTexture texture = new GVRBitmapTexture(gContext, bitmapTextureEmpty);
            int id =0;
            for(int j=0;j<ITEMS_PER_COLUMN;j++) {
                for(int i=0;i<ITEMS_PER_ROW;i++){
                    float locx = startX + stepX * i, locy = startY - stepY * j;
//                    Log.d(TAG, "start i j="+i+" "+j+" x,y=" + locx + "," + locy);
                    tutorialItems[id] = new TutorialItem(gContext, id, ITEM_SIZE, locx, locy, texture, BUTTON_TYPE, BUTTON_TYPE_ITEM);
                    root.addChildObject(tutorialItems[id].root);
                    id++;
                    }
                }
            }

    private void createHeader(){
            float sizeX = ITEMS_PER_ROW*(ITEM_SIZE[0]+ITEMS_PAD_ROW)-ITEM_SIZE[0];
            if (sizeX<3){sizeX = 3;}
            float locY =  ITEMS_PER_COLUMN*(ITEM_SIZE[1]+ITEMS_PAD_COLUMN)/2+ITEMS_PAD_COLUMN;
            float size = FOTTER_HEIGHT*1.2f;
            // Create the Header
            GVRSceneObject item = new GVRSceneObject(gContext);
            item.getTransform().setPosition(0,locY,0);
            item.setName("Header");
            createLabel(item,false,"HeaderLabel", "Tutorial List Menu",sizeX,size,0,0);
            root.addChildObject(item);
        }

    void getNextPage(boolean next){
        Log.d(TAG,"start change page");
        int nextPage =curPage;
        if(next){if(curPage<totalPages){nextPage++;}
        }else{if(curPage>1){nextPage--;}}
        if(curPage!=nextPage){
            curPage=nextPage;
            showItems();
        }
    }

    public void onTouchEvent(String[] tag){
//            Log.d(TAG,"click on Tutorial id"+tag[2]+" in position "+tag[1]);
            switch (tag[1]) {
                case "foot_prev": getNextPage(false);  break;
                case "foot_next": getNextPage(true);  break;
                case "tutorials_all": filterTutorials("all");  break;
                case "tutorials_saved": filterTutorials("saved");  break;
                case "tutorials_new": filterTutorials("new");  break;
                case BUTTON_TYPE_ITEM: showItemDetails(tag);
                    break;
                default: break;
            }
        }

    private void filterTutorials(String filter){
            Log.d(TAG,"Start filter tutorials "+filter);

        }

    private void showItemDetails(String[] tag){
            selectedTutorialNumber = Integer.parseInt(tag[2]);
            Log.d(TAG,"click on Tutorial id "+tag[3]+" in position "+tag[2]);
            JSONObject jObj = tutorialItems[selectedTutorialNumber].jsonObject;
            String txt= "";
            String tags="No data\n";
            try{
                JSONArray arr =  jObj.getJSONArray("Tags");
                for(int i=0,k=0; i<arr.length();i++,k++){
                    if(i>0){tags=tags.concat(",");}
                    tags=tags.concat(arr.getString(i));
                }tags=tags.concat("\n");
            }catch (Exception e){Log.d(TAG,"Can not extract tags from Tutorial json "+tag[3]);}
            for(int i=0;i<detailsText.length;i++){
                    String str = "No data\n";
                    if(detailsText[i].equals("Tags")){
                        txt = txt.concat(tags);
                    }else{
                        try{ str = detailsText[i]+": "+jObj.getString(detailsText[i])+"\n";
                        }catch (Exception e){Log.d(TAG,"Can not extract "+detailsText[i]+" from Tutorial json "+tag[3]);}
                        txt = txt.concat(str);
                    }}
            GVRTextViewSceneObject item = (GVRTextViewSceneObject) root.getSceneObjectByName("RightPanelText");
            item.setTextSize(4);
            item.setText(txt);
            GVRSceneObject btn = root.getSceneObjectByName("lp_action"); if(!btn.isEnabled()){btn.setEnable(true);}
        }

    private void hideItemDetails(){
            selectedTutorialNumber = -1;
            GVRTextViewSceneObject item = (GVRTextViewSceneObject) root.getSceneObjectByName("RightPanelText");
            item.setTextSize(4);
            item.setText(DEFAULT_MENU_TEXT);
            GVRSceneObject btn = root.getSceneObjectByName("lp_action"); if(btn.isEnabled()){btn.setEnable(false);}
        }

    private void createLeftPanel(){
            float width = ITEM_SIZE[0]*1.2f;
            float sizeY = FOTTER_HEIGHT;
            float locX = - ITEMS_PER_ROW*(ITEM_SIZE[0]+ITEMS_PAD_ROW)/2 - ITEMS_PAD_ROW;
            GVRSceneObject item = new GVRSceneObject(gContext);
            item.getTransform().setPosition(locX,0,0);
            float angle = (float) toDegrees(atan(-locX/5));
            item.getTransform().rotateByAxis(angle,0,1,0);
            item.setName("LeftPanel");
            float locstepY = width*0.4f;
            float locStartY = locstepY*2f;
            createLabel(item,true,"tutorials_all", "All Tutorials", width,sizeY,-width/2,locStartY);
            locStartY=locStartY-locstepY;
            createLabel(item,true,"tutorials_saved", "Installed", width,sizeY,-width/2,locStartY);
            locStartY=locStartY-locstepY;
            createLabel(item,true,"tutorials_new", "Not installed", width,sizeY,-width/2,locStartY);

        root.addChildObject(item);
    }

    private void createRightPanel(){
            float width = ITEM_SIZE[0]*1.5f;
            float locX = ITEMS_PER_ROW*(ITEM_SIZE[0]+ITEMS_PAD_ROW)/2+ITEMS_PAD_ROW;
            float height = (ITEMS_PER_COLUMN*(ITEM_SIZE[1]+ITEMS_PAD_COLUMN)-ITEMS_PAD_COLUMN)*0.5f;
            GVRSceneObject item = new GVRSceneObject(gContext);
            item.getTransform().setPosition(locX,0,0);
            float angle = (float) toDegrees(atan(-locX/5));
            item.getTransform().rotateByAxis(angle,0,1,0);
            item.setName("RightPanel");
            GVRSceneObject itemBack = new GVRSceneObject(gContext,width,height,new GVRBitmapTexture(gContext, createEmptyTexture(10,10)));
            itemBack.getTransform().setPosition(width/2,height*0.25f,-0.01f);
            item.addChildObject(itemBack);
            float textPad = width*0.2f;
            GVRTextViewSceneObject child = new GVRTextViewSceneObject(gContext,width-textPad,height*0.6f,DEFAULT_MENU_TEXT);
            child.getTransform().setPosition(width/2,height*0.25f,0);
            child.setName("RightPanelText");
            child.setBackgroundColor(EMPTY_ITEM_COLOR);
            child.setTextSize(6);
//            child.getGravity();
            child.setTextColor(BUTTON_COLOR_TXT);
            item.addChildObject(child);
            float size = FOTTER_HEIGHT*1.1f;
            GVRSceneObject btn = createLabel(item,true,"lp_action", "Install", width/2,size,width/2,-height*0.5f);
            btn.setEnable(false);
            root.addChildObject(item);
        }

    private void createFooter(){
            float sizeX = ITEMS_PER_ROW*(ITEM_SIZE[0]+ITEMS_PAD_ROW)-ITEM_SIZE[0];
            // set minimum width for footer
            if (sizeX<3){sizeX = 3;}
            float locY =  -ITEMS_PER_COLUMN*(ITEM_SIZE[1]+ITEMS_PAD_COLUMN)/2-ITEMS_PAD_COLUMN;
            // Create the Fotter background
            GVRSceneObject item = new GVRSceneObject(gContext,sizeX,FOTTER_HEIGHT,new GVRBitmapTexture(gContext, createEmptyTexture(sizeX,FOTTER_HEIGHT)));
            item.getTransform().setPosition(0,locY,0);
            item.setName("Footer");
            float size = FOTTER_HEIGHT*1.1f;
            createLabel(item,true,"foot_prev", "<", size,size,(-sizeX+size)/2,0);
            createLabel(item,true,"foot_next", ">", size,size,(sizeX-size)/2,0);
            createLabel(item,false,"pagesList",  Integer.toString(curPage)+"/"+Integer.toString(totalPages),0,size*0.9f,0,0);
            root.addChildObject(item);
        }

    private void updateLabel(String name, String txt){
            GVRSceneObject item = root.getSceneObjectByName(name);
            GVRTexture texture = new GVRBitmapTexture(gContext, createLabelTexture(0,FOTTER_HEIGHT,txt));
            item.getRenderData().getMaterial().setMainTexture(texture);
        }

    private GVRSceneObject createLabel(GVRSceneObject root, boolean isButton, String name, String txt, float width, float height, float locx, float locy){
            Bitmap bitmap = createLabelTexture(width,height,txt);
            if(width==0){width = bitmap.getWidth()/100;}
            GVRSceneObject item = new GVRSceneObject(gContext,width,height,new GVRBitmapTexture(gContext, bitmap));
            item.setName(name); String[] tag = {BUTTON_TYPE,name}; item.setTag(tag);
            item.getTransform().setPosition(locx,locy,0.01f);
            if (isButton){item.attachComponent(new GVRSphereCollider(gContext));}
            root.addChildObject(item);
            return item;
        }

    private Bitmap createEmptyTexture(float sizeWidth, float sizeHeight){
                int x = (int) (sizeWidth*300);
                int y = (int) (sizeHeight*300);
                int[] colors = new int[x*y];
                Arrays.fill(colors, 0, x * y, EMPTY_ITEM_COLOR);
//                Arrays.fill(colors, 0, x * y, Color.argb(255,219,181,255));
                Bitmap bitmapAlpha = Bitmap.createBitmap(colors, x, y, Bitmap.Config.ARGB_8888);
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

}
