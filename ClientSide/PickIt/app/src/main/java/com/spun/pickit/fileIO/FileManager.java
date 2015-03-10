package com.spun.pickit.fileIO;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by BJClark on 3/10/2015.
 */
public class FileManager {
    private Activity activity;

    //region Constructors
    public FileManager(Activity activity){
        this.activity = activity;
    }
    //endregion

    /**
     *
     * Create your own regions for specific file IO based on the page.
     * We may want to store as much data/metadata locally as we canto reduce database calls
     *
     */

    //region ...Credentials
    public ArrayList<String> readSavedCredentials(){
        final String FILE_NAME = "credentials.txt";
        ArrayList<String> credentials = new ArrayList<>();

        try {
            InputStream inputStream = activity.openFileInput(FILE_NAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();

                String fromFile = stringBuilder.toString();

                JSONObject json = new JSONObject(fromFile);

                credentials.add(json.getString("Username"));
                credentials.add(json.getString("Password"));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return credentials;
    }

    public boolean credentialFileExists(){
        final String FILE_NAME = "credentials.txt";
        boolean fileExists = false;

        String[] files = activity.fileList();
        for (String file : files) {
            if (file.equals(FILE_NAME)) {
                fileExists = true;
                break;
            }
        }

        return fileExists;
    }

    public void deleteCredentials(){
        final String FILE_NAME = "credentials.txt";
        String[] files = activity.fileList();
        for (String file : files) {
            if (file.equals(FILE_NAME)) {
                activity.deleteFile(FILE_NAME);
            }
        }
    }

    public void saveCredentials(String username, String password){
        final String FILE_NAME = "credentials.txt";
        JSONObject temp = new JSONObject();

        try {
            temp.put("Username", username);
            temp.put("Password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(temp.length() != 0){
            final JSONObject credentials = temp;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileOutputStream os = null;

                    try{
                        os = activity.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                        os.write(credentials.toString().getBytes());
                    }catch(Exception e){
                        e.printStackTrace();
                    }finally{
                        if(os != null)
                            try {
                                os.flush();
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }).start();
        }
    }
    //endregion

}
