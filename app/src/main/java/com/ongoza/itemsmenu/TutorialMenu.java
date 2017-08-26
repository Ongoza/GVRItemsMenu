package com.ongoza.itemsmenu;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import org.gearvrf.GVRBitmapTexture;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Arrays;

import static java.lang.Math.floor;

/**
 * Created by os on 8/25/17.
 */

    class TutorialMenu extends GVRSceneObject {
        GVRContext gContext;
        GVRSceneObject root;
        private static final String TAG = MainActivity.getTAG();
        JSONArray tutorialsArray = new JSONArray();
        int curPage = 1;
        int totalPages = 1;
        GVRSceneObject main;
        int itemsPerColumn = 3;
        float itemsPadColumn = 0.5f;
        int itemsPerRow = 3;
        float itemsPadRow = 0.5f;
        int itemsPerPage = itemsPerColumn*itemsPerRow;
        TutorialItem[] tutorialItems = new TutorialItem[itemsPerPage];
        float[] itemSize = new float[]{1,1};

        public  TutorialMenu(GVRContext gContext) {
            super(gContext);
            this.gContext = gContext;
            Log.d(TAG,"start menu");
            root = new GVRSceneObject(gContext);
            root.getTransform().setPosition(0,0,-5);
            Log.d(TAG,"start menu2");
            loadItemsLocal();
            Log.d(TAG,"start menu3");
            createEmptyItems();
            Log.d(TAG,"start menu4");

        }

        public void show(GVRSceneObject main){ this.main = main;
            main.addChildObject(root);}

        public void hide(){ main.removeChildObject(root); }

        private void loadItemsLocal(){
            for(int i=0; i<9;i++){
                String jStr=
                        "{\"_id\":"+i
                                +",\"name\":\"Tutorial "+i+"\""
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
            Bitmap bitmapTextureEmpty = createEmptyTexture();
            float startX, startY,stepX=itemsPadRow+itemSize[0], stepY=itemsPadColumn+itemSize[1];
            float middleX = itemsPerRow/2, middleXFloor = (float) floor(middleX);
            float middleY = itemsPerRow/2, middleYFloor = (float) floor(middleY);
            if(middleX>middleXFloor){startX = -middleXFloor*stepX;
            }else{startX = -stepX/2-middleXFloor*stepX;}
            if(middleY>middleYFloor){startY = -middleYFloor*stepY;
            }else{startY = -stepY/2-middleYFloor*stepY;}
            Log.d(TAG,"start x,y="+startX+","+startY);
            GVRTexture texture = new GVRBitmapTexture(gContext, bitmapTextureEmpty);
            for(int i=0;i<itemsPerPage;i++){
                float locx = startX+stepX*i, locy = startY+stepY*i;
                tutorialItems[i] = new TutorialItem(gContext, i, itemSize, locx, locy, texture);
                root.addChildObject(tutorialItems[i]);
                }
            }

        private Bitmap createEmptyTexture(){
                int[] colors = new int[100 * 100];
//                Arrays.fill(colors, 0, 100 * 100, Color.argb(255,129,81,4));
                Arrays.fill(colors, 0, 100 * 100, Color.argb(255,219,181,255));
                Bitmap bitmapAlpha = Bitmap.createBitmap(colors, 100, 100, Bitmap.Config.ARGB_8888);
                return bitmapAlpha;
            }
    }



