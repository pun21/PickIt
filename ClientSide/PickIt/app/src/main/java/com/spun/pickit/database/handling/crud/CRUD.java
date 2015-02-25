package com.spun.pickit.database.handling.crud;

import android.os.Debug;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.InputStreamReader;

public abstract class CRUD implements CRUDable{
    protected static final String URL = "http://www.bcdev.me:8080/";

//for easy pickup
//protected String readExtension(){
//    String extension;
//    return extension;
//}
//    protected String deleteExtension(){
//        String extension;
//        return extension;
//    }
//    protected String updateExtension(){
//        String extension;
//        return extension;
//    }
//    protected String createExtension(){
//        String extension;
//        return extension;
//    }
//
//    protected void readPrimitive(JSONObject json){}
//    protected void deletePrimitive(JSONObject json){}
//    protected void updatePrimitive(JSONObject json){}
//    protected void createPrimitive(JSONObject json){}

    protected abstract String readExtension();
    protected abstract String deleteExtension();
    protected abstract String updateExtension();
    protected abstract String createExtension();

    protected abstract void readPrimitive(JSONObject json);
    protected abstract void deletePrimitive(JSONObject json);
    protected abstract void updatePrimitive(JSONObject json);
    protected abstract void createPrimitive(JSONObject json);

    //connect the extension to the URL of the server
    public final JSONObject connect(String urlString){
        JSONObject json = new JSONObject();
        BufferedReader br = null;
        URL url;
        try {
            url = new URL(urlString);
            br = new BufferedReader( new InputStreamReader (url.openStream()));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            json = new JSONObject(sb.toString());

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            //close the BufferReader
            try {
                if (br != null) {
                    br.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        return json;
    }

    public final void read(){
        String extension = this.URL + readExtension();
        JSONObject json = this.connect(extension);
        readPrimitive(json);
    }

    public final void delete(){
        String extension = this.URL + deleteExtension();
        JSONObject json = this.connect(extension);
        deletePrimitive(json);
    }
    public final void update(){
        String extension = this.URL + updateExtension();
        JSONObject json = this.connect(extension);
        updatePrimitive(json);
    }
    public final void create(){
        String extension = this.URL + createExtension();
        JSONObject json = this.connect(extension);
        createPrimitive(json);
    }
}
