package com.ongoza.itemsmenu;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.gearvrf.GVRContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
/**
 * Created by os on 8/30/17.
 */

public class ConnectionManager {
    private final String TAG = MainActivity.getTAG();
    private final String IP = "192.168.1.30";
    static final String CMD_INSERT = "insert";
    static final String CMD_TAKE = "find";
    private final String USER_AGENT = "Mozilla/5.0";

    private final String urlStr = "http://"+IP+":27080/local/dbTutorials/_";
    HashMap<String,ConnectionString> connectionThreads = new HashMap<>();
    HashMap<String,ConnectionVideo> connectionThreadsVideo = new HashMap<>();
    TutorialMenu tutorialMenu;
    GVRContext gContext;
    HashMap<String,Integer> connectionThreadsProgress = new HashMap<>();

    public ConnectionManager(TutorialMenu tutorialMenu, GVRContext gContext){
        this.tutorialMenu = tutorialMenu;
        this.gContext = gContext;
    }
    private boolean startDownloadItems(String name, String cmd, String data){
        boolean result = false;
        ConnectionString newConnection = new ConnectionString();
        try {
          //  Log.d(TAG, "Start connect to DB "+ url);
            newConnection.execute(name, cmd, data);
            connectionThreads.put(name,newConnection);
//            connectionThreadsProgress.put(name,0);
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "Not connected to DB "); }
        return result;
    }

    public void startDownload(String command, String name, String data){
//        if(tutorialMenu==null){tutorialMenu=menu;}
            switch (command){
                case "takeAllTutorials":
                    startDownloadItems(name,CMD_TAKE,data);
                    break;
                case "takeVideoTutorials":
                    startDownloadVideo(name,data);
                    break;
                default: break;
            }
    }

    private void startDownloadVideo(String tutorialID,String videoURL){
        ConnectionVideo newConnection = new ConnectionVideo();
        try {
            //  Log.d(TAG, "Start connect to DB "+ url);
            newConnection.execute(tutorialID, videoURL);
            connectionThreadsVideo.put(tutorialID,newConnection);
            connectionThreadsProgress.put(tutorialID,0);
        } catch (Exception e) {
            Log.d(TAG, "Not connected to DB "); }

    }

    public void progressDownload(String name,int result){
        try{
            connectionThreadsProgress.put(name,result);
        }catch (Exception e){ Log.d(TAG,"Can not update progress for "+name);}
    }

    public void finishDownload(String name, String result) {
        try {
            Log.d(TAG,"Downloaded "+name + " result ="+result);
            JSONObject jObj = new JSONObject(result);
            JSONArray jArr = jObj.getJSONArray("results");
            tutorialMenu.updateTutorials(jArr);
            connectionThreads.remove(name);
            connectionThreadsProgress.remove(name);
        } catch (Exception e) {
            Log.d(TAG, "Can not update progress for " + name+ " result ="+result);
        }
    }


    public class ConnectionString extends AsyncTask<String, Integer, String> {

        String outputResult;
        String name;

        @Override protected String doInBackground(String... data) {
            outputResult = ""; String output = "";
            // Log.d(TAG, " start save sendingString=" + data[0]+ data[1]+" "+CmdTake );
            try {  name = data[0];
                //Log.d(TAG, " start save sendingString=" + sendingString);
                String fullUrl = urlStr+data[1];
                if(data[1].equals(CMD_TAKE)){fullUrl=fullUrl+data[2];}
                URL url = new URL(fullUrl);
                //Log.d(TAG, " start save url=" + url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                if(!data[1].equals(CMD_TAKE)){
                    con.setDoOutput(true); con.setRequestMethod("POST");}
                else{con.setRequestMethod("GET");  }
                con.setReadTimeout(600); // millis
                con.setConnectTimeout(1000); // millis
                con.setRequestProperty("Accept-Language", "UTF-8");
                con.setRequestProperty("Accept-Charset", "UTF-8");
                con.setRequestProperty("User-Agent", USER_AGENT);
                con.connect();
                if(!data[1].equals(CMD_TAKE)){
                    String sendingString = "docs=["+data[2]+']';
                    OutputStream wd = con.getOutputStream();
                    wd.write(sendingString.getBytes());
                    wd.flush();}
                String line;
                try { BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    while ((line = in.readLine()) != null){ output += line;}
                } finally {  con.disconnect(); }
                // Log.d(TAG, " start save 2 response="+output);
                outputResult = output;
            }catch(Exception e){
//            Log.d(TAG, "Not connected to DB "+ Arrays.toString(e.getStackTrace()));
                Log.d(TAG, "Not connected to DB ");}
            return output;
        }

        @Override protected void onProgressUpdate(Integer... progress) {
            // your code to update progress
            Log.d(TAG,"Progress="+progress[0]);
            progressDownload(name,progress[0]);
        }

        @Override protected void onPostExecute(String result) {
            // Log.d(TAG,"Downloaded " + result);
            finishDownload(name,result);
        }
    }

    public class ConnectionVideo extends AsyncTask<String, Integer, String> {

     //   String outputResult;
        String name;
        File videoStorage = null;
        String outputFile = null;

        @Override protected String doInBackground(String... data) {
           // outputResult = "";
            // String output = "";
            // Log.d(TAG, " start save sendingString=" + data[0]+ data[1]+" "+CmdTake );
            try {  name = data[0];
                //Log.d(TAG, " start save sendingString=" + sendingString);
                String fullUrl = data[1];
                URL url = new URL(fullUrl);
                Log.d(TAG, " start save url=" + url.toString());
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                Log.d(TAG, "Server returned HTTP " + con.getResponseCode()   + " " + con.getResponseMessage());
                outputFile = "test1.mp4";//Create Output file in Main File
                FileOutputStream fos = gContext.getContext().openFileOutput(outputFile, Context.MODE_PRIVATE);
                InputStream is = con.getInputStream();//Get InputStream for connection
                byte[] buffer = new byte[1024];//Set buffer type
                int len1 = 0;//init length
                while ((len1 = is.read(buffer)) != -1) {
                   // Log.d(TAG, " start save answer77="+len1);
                    fos.write(buffer, 0, len1);//Write new file
                }
                //Close all connection after doing task
                fos.close();
                is.close();

            }catch(Exception e){
//            Log.d(TAG, "Not connected to DB "+ Arrays.toString(e.getStackTrace()));
                Log.d(TAG, "Not connected to DB ");}
            return null;
        }

        @Override protected void onProgressUpdate(Integer... progress) {
            // your code to update progress
            Log.d(TAG,"Progress="+progress[0]);
            progressDownload(name,progress[0]);
        }

        @Override protected void onPostExecute(String result) {
            try {
                if (outputFile != null) {
                    Log.d(TAG, "Downloaded ok");
//            finishDownload(name,result);
                }
            }catch (Exception e) {
                Log.d(TAG,"Error save file=");
                e.printStackTrace();}
        }
    }
}
