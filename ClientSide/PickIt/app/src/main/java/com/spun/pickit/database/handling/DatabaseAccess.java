package com.spun.pickit.database.handling;

import android.app.Activity;
import com.spun.pickit.database.handling.crud.PasswordValidation;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class DatabaseAccess {
    public boolean validatePassword(String username, String password) {
        PasswordValidation passwordValidation = new PasswordValidation(username,password);
        DataAccess access = new DataAccess(passwordValidation.read());

        JSONObject json = access.getJson();

        boolean pass = false;
        try{
            pass = json.get("success") == 1;
        }catch(JSONException e){
            try {
                json = json.getJSONObject("Result");
                pass = json.get("success") == 1;
            } catch (JSONException e1) {
                e.printStackTrace();
                e1.printStackTrace();
            }
        }

        return pass;
    }

    class DataAccess {
        private String url;
        private JSONObject json = null;
        private Thread readerThread;

        public DataAccess(String url){
            this.url = url;
            readerThread = new AsyncReaderThread();
            readerThread.start();
        }

        public JSONObject getJson(){
            while(json ==  null){
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return json;
        }

        class AsyncReaderThread extends Thread{

            @Override
            public void run(){
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                DefaultHttpClient client = new DefaultHttpClient();

                InputStream content = null;
                HttpGet request;
                String response = "";

                try {
                    request = new HttpGet( url );
                    response = client.execute( request, responseHandler );
                    json = new JSONObject(response);
                } catch ( Exception e ) {
                    e.printStackTrace();
                }finally {
                    if (content != null)
                        try {
                            content.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                }


                if(json == null){
                    json = new JSONObject();
                    try {
                        json.put("Successful", "0");
                        json.put("Result", "!ERROR!");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    JSONObject temp = new JSONObject();
                    try{
                        temp.put("Successful", "1");
                        temp.put("Result", json);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }

                    json = temp;
                }
            }
        }
    }
}
