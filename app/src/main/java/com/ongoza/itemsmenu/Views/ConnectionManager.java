package com.ongoza.itemsmenu.Views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.ongoza.itemsmenu.MainActivity;

import org.gearvrf.GVRContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.TimeZone;

/**
 * Created by os on 8/30/17.
 * // list server commands:
 // curl --data 'docs=[{"x":1}]' 'http://localhost:27080/local/dbTutorials/_insert'
 // curl --data 'docs={}' 'http://localhost:27080/local/dbTutorials/_find'
 // curl -X GET 'http://localhost:27080/local/dbTutorials/_login?l=os@ongoza.com&p=11'
 // curl -X GET 'http://localhost:27080/local/dbTutorials/_register?l=os@ongoza.com&p=11'
 // curl -X GET 'http://localhost:27080/local/dbTutorials/_unregister?l=os@ongoza.com&p=11'
 // curl -X GET 'http://localhost:27080/local/dbTutorials/_logout?l=os@ongoza.com&s=11'
 */

public class ConnectionManager {
    private final String TAG = MainActivity.getTAG();
    private final String USER_AGENT = "VRTE/4.0";
    private final String IP = "192.168.1.30";
//    static final String CMD_INSERT = "insert";
    static final String CMD_TAKE = "find";
//    static final String CMD_LOGIN = "login";
//    static final String CMD_REGISTER = "register";
    private String guid;
    public String queryLogin;

    private final String urlStr = "http://"+IP+":27080/local/dbTutorials/_";
    HashMap<String,TakeServerString> connectionThreads = new HashMap<>();
    HashMap<String,ConnectionVideo> connectionThreadsVideo = new HashMap<>();
    TutorialMenu tutorialMenu;
    GVRContext gContext;
    HashMap<String,Integer> connectionThreadsProgress = new HashMap<>();

    public ConnectionManager(TutorialMenu tutorialMenu, GVRContext gContext, String id){
        this.tutorialMenu = tutorialMenu;
        this.gContext = gContext;
        this.guid = id;
    }

    public void setLoginQuery(String str){this. queryLogin = str;
        Log.d(TAG,"set qeryLogin="+str);
    }

    private boolean startDownloadItems(String name, String cmd, String data){
        boolean result = false;
        ConnectionString newConnection = new ConnectionString();
        try {
          //  Log.d(TAG, "Start connect to DB "+ url);
            newConnection.execute(name, cmd, data);
            //connectionThreads.put(name,newConnection);
//            connectionThreadsProgress.put(name,0);
            result = true;
        } catch (Exception e) {
            Log.d(TAG, "Not connected to DB "); }
        return result;
    }

    // public void startDownload(String command, String type, String data){
    // connectionManager.startDownload("takeVideoTutorials","#AAA","http://192.168.1.30/2/test1.mp4");
    // url=http://192.168.1.30:27080/local/dbTutorials/_login?l=os&p=11


    public void startDownload(String command, String type, String data){
//        if(tutorialMenu==null){tutorialMenu=menu;}
            switch (command){
                case "takeAllTutorials":
                   // startDownloadItems(type,data);
                    break;
                case "takeVideoTutorials":
                    startDownloadVideo(type,data);
                    break;
                case "cmdToServer":
                    //startSendCmdToServer(command, data,"");
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

    public boolean startSendToServer(String cmd, String type, String data, String post){
        boolean result = false;
        TakeServerString newConnection = new TakeServerString();
        try {
           // Log.d(TAG, "Start connecting to Server "+cmd+" "+data+" "+type+" "+post+" login="+queryLogin);
            newConnection.execute(cmd,type,data,post);
            connectionThreads.put(data,newConnection);
            result = true;
        } catch (Exception e){Log.d(TAG, "Does not connected to Server "); }
        return result;
    }


    public void resultServerCommand(String name, String cmd, String result){
        //Log.d(TAG,"Server has said on command "+name + " result ="+result);
        tutorialMenu.serverAnswerHandler(name,cmd,result,queryLogin);
    }

    public class TakeServerString extends AsyncTask<String, Integer, String> {

        String outputResult;
        String cmd;
        String link;
        String post;

        @Override protected String doInBackground(String... data) {
            outputResult = ""; String output = "";
            if(!queryLogin.equals("")) {
                 Log.d(TAG, " start save sendingString=" + data[0]+ data[1]+" "+data[2]+" "+data[3]+" login="+queryLogin );
                try {
                    cmd = data[0];
                    link = data[2];
                    post = data[3];
                    String type = data[1];
                    //Log.d(TAG, " start save sendingString=" + sendingString);
                    URL url = new URL(urlStr + cmd + queryLogin + link + "&id=" + guid);
                    Log.d(TAG, " start save url=" + url.toString());
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod(type);
                    if (type.equals("POST")) {con.setDoOutput(true);}
                    con.setReadTimeout(5000); // millis
                    con.setConnectTimeout(5000); // millis
                    con.setRequestProperty("Accept-Language", "UTF-8");
                    con.setRequestProperty("Accept-Charset", "UTF-8");
                    con.setRequestProperty("User-Agent", USER_AGENT);
                    con.connect();
                    if (type.equals("POST")) {
                        OutputStream wd = con.getOutputStream();
                        wd.write(post.getBytes());
                        wd.flush();
                    }
                    String line;
                    Log.d(TAG, "connect to server.");
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                        while ((line = in.readLine()) != null) {
                            output += line;
                        }
                    } finally {
                        con.disconnect();
                    }
                    //Log.d(TAG, " start save 2 response=" + output);
                    outputResult = output;
                } catch (Exception e) {
                    Log.d(TAG, "Not connected to Server " + Arrays.toString(e.getStackTrace()));
                    Log.d(TAG, "Server connection error cmd=" +cmd+" link="+link+" query="+post);
                    output = "[\"client\",-1,\"Error!\\nClient can not connect to server.\"]";
                }
            }else{ Log.d(TAG, "No login and password data!"); }
            return output;
        }

//        @Override protected void onProgressUpdate(Integer... progress) {
//            // your code to update progress
//            Log.d(TAG,"Progress="+progress[0]);
//            progressDownload(name,progress[0]);
//        }

        @Override protected void onPostExecute(String result) {
            //Log.d(TAG,"Downloaded " + result);
            if(!result.equals("")){resultServerCommand(cmd,link,result);
            }else{
                Log.d(TAG,"Downloaded empty result.");
            }
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
