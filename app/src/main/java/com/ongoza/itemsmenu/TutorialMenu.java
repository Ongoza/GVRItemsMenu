//todo Add "show item details" window


package com.ongoza.itemsmenu;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.util.Size;

import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRCollider;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRSphereCollider;
import org.gearvrf.GVRTexture;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import static java.lang.Math.floor;

/**
 * Created by os on 8/25/17.
 */

class TutorialMenu extends GVRSceneObject {
    private String BUTTON_TYPE;
    private final String BUTTON_TYPE_ITEM = "tutItem";
    private final int BUTTON_COLOR_TXT = Color.argb(255,255,250,240);
    private final int ITEMS_PER_COLUMN = 3;
    private final float ITEMS_PAD_COLUMN = 0.5f;
    private final int ITEMS_PER_ROW = 3;
    private final float ITEMS_PAD_ROW = 0.5f;
    private final float[] ITEM_SIZE = new float[]{1.5f,1f};
    private final int EMPTY_ITEM_COLOR =  Color.argb(255,70,60,50);
    private final float FOTTER_HEIGHT = 0.3f;


        GVRContext gContext;
        GVRSceneObject root;
        private static final String TAG = MainActivity.getTAG();
        JSONArray tutorialsArray = new JSONArray();
        int curPage = 1;
        int totalPages = 1;
        GVRSceneObject main;


        int itemsPerPage = ITEMS_PER_COLUMN*ITEMS_PER_ROW;
        TutorialItem[] tutorialItems = new TutorialItem[itemsPerPage];


        public  TutorialMenu(GVRContext gContext, String btnType) {
            super(gContext);
            this.gContext = gContext;
            BUTTON_TYPE = btnType;
            Log.d(TAG,"start menu");
            root = new GVRSceneObject(gContext);
            root.getTransform().setPosition(0,0,-5);

            createFooter();
            loadItemsLocal();
            createEmptyItems();
            showItems();


        }

        private void showItems(){
//            Log.d(TAG,"start show items for current page "+curPage);
            totalPages = tutorialsArray.length()/itemsPerPage+1;
            for(int j=0, i=(curPage-1)*itemsPerPage;j<itemsPerPage;i++,j++){
                tutorialItems[j].removeItem();
                if(i<tutorialsArray.length()){
                    try{ JSONObject curItem = tutorialsArray.getJSONObject(i);
                        tutorialItems[j].addItem(curItem);
                    }catch (Exception e){Log.d(TAG," can not parse json object "+i+" menu="+j);}
                    }}
            updateLabel("pagesList",String.valueOf(curPage)+"/"+String.valueOf(totalPages));
            }

        public void show(GVRSceneObject main){
            Log.d(TAG,"start show tutorial menu ");
            this.main = main;
            main.addChildObject(root);
//            updateLabel("pagesList","Hello");
        }

        public void hide(){ main.removeChildObject(root); }

        private void loadItemsLocal(){
            for(int i=0; i<20;i++){
                String jStr=
                        "{\"_id\":\"AAAA"+i+"\""
                                +",\"name\":\"Tutorial "+i+"\""
                                +",\"backColor\":\"#222222\""
                                +",\"fontSize\":\"30\""
                                +",\"fontColor\":\"#FFFFFF\""
                                +",\"author\":\"Oleg Skuibida\""
                                +",\"tags\":[\"Common\",\" Cooking\"]"
                                +",\"course\":\"Common\""
                                +",\"price\":\"Free\""
                                +",\"size\":\"10 MB\""
                                +"}";
                try {JSONObject item = new JSONObject(jStr); tutorialsArray.put(item);
                }catch (Exception e){ Log.d(TAG,"Error parse json");
                }
            }
//            Log.d(TAG,"tutorialsArray ="+tutorialsArray);
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

        void onTouchEvent(String[] tag){
//            Log.d(TAG,"click on Tutorial id"+tag[2]+" in position "+tag[1]);
            switch (tag[1]) {
                case "prev": getNextPage(false);  break;
                case "next": getNextPage(true);  break;
                case BUTTON_TYPE_ITEM: showItemDetails(tag);
                    break;
                default: break;
            }
        }

        private void showItemDetails(String[] tag){
            Log.d(TAG,"click on Tutorial id "+tag[3]+" in position "+tag[2]);
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
            float size = FOTTER_HEIGHT;
            createLabel(item,true,"prev", "<", size,size,(-sizeX+size)/2,0);
            createLabel(item,true,"next", ">", size,size,(sizeX-size)/2,0);
            createLabel(item,false,"pagesList",  Integer.toString(curPage)+"/"+Integer.toString(totalPages),0,size*0.9f,0,0);
            root.addChildObject(item);
        }

        private void updateLabel(String name, String txt){
            GVRSceneObject item = root.getSceneObjectByName(name);
            GVRTexture texture = new GVRBitmapTexture(gContext, createLabelTexture(0,FOTTER_HEIGHT,txt));
            item.getRenderData().getMaterial().setMainTexture(texture);
        }

        private void createLabel(GVRSceneObject root, boolean isButton, String name, String txt, float width, float height, float locx, float locy){
            Bitmap bitmap = createLabelTexture(width,height,txt);
            if(width==0){width = bitmap.getWidth()/100;}
            GVRSceneObject item = new GVRSceneObject(gContext,width,height,new GVRBitmapTexture(gContext, bitmap));
            item.setName(name); String[] tag = {BUTTON_TYPE,name}; item.setTag(tag);
            item.getTransform().setPosition(locx,locy,0.01f);
            if (isButton){item.attachComponent(new GVRSphereCollider(gContext));}
            root.addChildObject(item);
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
            Rect r = new Rect(); int w=10, h=(int) (height*100);
            if(width!=0){ w = (int) (width*100);
            }else{
                Paint testPaint = new Paint();
                testPaint.setTextSize(h);
//                testPaint.setTextAlign(Paint.Align.CENTER);
//                Log.d(TAG,"paint "+str+"width="+testPaint.measureText(str));
                w = (int) (testPaint.measureText(str)*10); // round
            }
            Bitmap bitmapAlpha = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmapAlpha);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            c.drawColor(EMPTY_ITEM_COLOR);
            paint.setColor(BUTTON_COLOR_TXT);
            paint.setTextSize(h);
            paint.getTextBounds(str, 0, str.length(), r);
            float x = w/2f - r.width() / 2f - r.left;
            float y = h/2f - r.height() / 2f - r.top;
            c.drawText(str,x,y,paint);
//            paint.setAlpha(255);
        return bitmapAlpha;
    }
    }



