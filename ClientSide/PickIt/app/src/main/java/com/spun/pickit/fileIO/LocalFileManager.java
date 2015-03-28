package com.spun.pickit.fileIO;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.spun.pickit.PickItApp;
import com.spun.pickit.model.Demographics;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class LocalFileManager {
    final String CREDENTIALS_FILE_NAME = "credentials.txt";

    private Activity activity;
    String demographicsFileName;
    String demographicsFilePath;

    //region Constructors
    public LocalFileManager(Activity activity){
        this.activity = activity;
    }
    //endregion

    /**
     *
     * Create your own regions for specific file IO based on the page.
     * We may want to store as much data/metadata locally as we canto reduce database calls
     *
     */

    //region ...Demographics
    public String getDemographicsFilePath(){
        return demographicsFilePath;
    }

    public boolean readSavedDemographics(){
        try {
            FileInputStream demographicsInputStream = activity.openFileInput(demographicsFileName);

            if ( demographicsInputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(demographicsInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                demographicsInputStream.close();

                String fromFile = stringBuilder.toString();

                JSONObject json = new JSONObject(fromFile);

                String birthday = json.getString("Birthday");
                String gender = json.getString("Gender");
                String ethnicity = json.getString("Ethnicity");
                String religion = json.getString("Religion");
                String politicalAffiliation = json.getString("Political Affiliation");

                Demographics demo = new Demographics(birthday, gender, ethnicity, religion, politicalAffiliation);
                ((PickItApp)activity.getApplication()).setDemographics(demo);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public boolean demographicsFileExists(){
        boolean fileExists = false;

        String[] files = activity.fileList();
        for (String file : files) {
            if (file.equals(demographicsFileName)) {
                fileExists = true;
                break;
            }
        }

        return fileExists;
    }

    public void deleteDemographics(){
        String[] files = activity.fileList();
        for (String file : files) {
            if (file.equals(demographicsFileName)) {
                activity.deleteFile(demographicsFileName);
            }
        }
    }

    public void saveDemographics(String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        JSONObject temp = new JSONObject();

        try {
            temp.put("Birthday", birthday);
            temp.put("Gender", gender);
            temp.put("Ethnicity", ethnicity);
            temp.put("Religion", religion);
            temp.put("Political Affiliation", politicalAffiliation);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(temp.length() != 0){
            final JSONObject demographics = temp;
            demographicsFileName = ((PickItApp)activity.getApplication()).getUsername()+"_demographics.json";

            try{
                File file = new File(Environment.getExternalStorageDirectory(), "Demographics");

                if(!file.exists())
                {
                    file.mkdirs();
                }

                File gpxFile = new File(file, demographicsFileName);

                if(gpxFile.exists()){
                    gpxFile.delete();
                }

                gpxFile.createNewFile();

                demographicsFilePath = gpxFile.getAbsolutePath();

                FileWriter writer = new FileWriter(gpxFile);
                writer.append(demographics.toString());
                writer.flush();
                writer.close();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                });
            }catch(Exception e){
                e.printStackTrace();

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity.getApplicationContext(), "Failed to save", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
    //endregion

    //region ...Credentials
    public ArrayList<String> readSavedCredentials(){

        ArrayList<String> credentials = new ArrayList<>();

        try {
            InputStream inputStream = activity.openFileInput(CREDENTIALS_FILE_NAME);

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
        boolean fileExists = false;

        String[] files = activity.fileList();
        for (String file : files) {
            if (file.equals(CREDENTIALS_FILE_NAME)) {
                fileExists = true;
                break;
            }
        }

        return fileExists;
    }

    public void deleteCredentials(){
        String[] files = activity.fileList();
        for (String file : files) {
            if (file.equals(CREDENTIALS_FILE_NAME)) {
                activity.deleteFile(CREDENTIALS_FILE_NAME);
            }
        }
    }

    public void saveCredentials(String username, String password){
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
                        os = activity.openFileOutput(CREDENTIALS_FILE_NAME, Context.MODE_PRIVATE);
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
