package com.example.amazonhdr;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";
    String VIDEO_KEY = "video_count";
    String SESSID_KEY =  "sess_id_count";

    String TVID_KEY =  "tv_id";
    String NAME_KEY = "name";

    String TRAIN_VIDEO_KEY = "train_video_count";

    String IS_TRAIN = "is_in_training";


    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(User user){
        // save session of user whenever user is logged in
        int id = user.getId();
        String name = user.getName();
        int sessID = user.getSessID();
        int tvID = user.getTVID();
        int videoCompleted = user.getVidCompleted();
        int trainVideoCompleted = user.getTrainVidCompleted();
        boolean isTrain = user.getIsTrain();
        editor.putInt(SESSION_KEY,id).commit();
        editor.putInt(TVID_KEY,tvID).commit();
        editor.putInt(VIDEO_KEY,videoCompleted).commit();
        editor.putString(NAME_KEY,name).commit();
        editor.putInt(SESSID_KEY,sessID).commit();
        editor.putInt(TRAIN_VIDEO_KEY,trainVideoCompleted).commit();
        editor.putBoolean(IS_TRAIN,isTrain).commit();

        //final String downloadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/scores";
        //System.out.println(downloadPath);
        //File folder = new File(downloadPath);
        //if (!folder.exists()) {
        //    boolean value = folder.mkdirs();
        //    System.out.println(value);
        //}


        String path_file = Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/Portrait/"+String.valueOf(id)+"_"+name+"_"+String.valueOf(sessID)+"_TV"+String.valueOf(tvID)+".txt";
        System.out.println(path_file);
        File scoreFile = new File (path_file);
        try {
            boolean value1 = scoreFile.createNewFile();
            System.out.println(value1);
            FileOutputStream outStream = new FileOutputStream(scoreFile,true);
            outStream.write(("Time Stamp"+","+"ID"+","+"Name"+","+"Session ID"+","+"VideoName"+","+"Score"+"\n").getBytes());
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getSession(){
        // return user whose session is saved
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }

    public int getTVID(){
        // return user whose session is saved
        return sharedPreferences.getInt(TVID_KEY,-1);
    }

    public int getVideoCount(){
        return sharedPreferences.getInt(VIDEO_KEY,-1);
    }

    public int getTrainVideoCount(){
        return sharedPreferences.getInt(TRAIN_VIDEO_KEY,-1);
    }

    public String getName(){
        return sharedPreferences.getString(NAME_KEY,null);
    }

    public int getSessID(){
        return sharedPreferences.getInt(SESSID_KEY,-1);
    }

    public void incrementVideoCount(){
        int videoIndex = sharedPreferences.getInt(VIDEO_KEY,-1);
        editor.putInt(VIDEO_KEY,videoIndex+1).commit();
    }

    public void incrementTrainVideoCount(){
        int videoIndex = sharedPreferences.getInt(TRAIN_VIDEO_KEY,-1);
        editor.putInt(TRAIN_VIDEO_KEY,videoIndex+1).commit();
    }

    public boolean isTrain()
    {
        return sharedPreferences.getBoolean(IS_TRAIN, false);
    }

    public void setIsTrain(Boolean isTrain)
    {
        editor.putBoolean(IS_TRAIN, isTrain).commit();
    }
    public void removeSession(){

        editor.putInt(SESSION_KEY,-1).commit();
        editor.putInt(VIDEO_KEY,-1).commit();
        editor.putString(NAME_KEY,null).commit();
        editor.putInt(SESSION_KEY,-1).commit();
    }

}
