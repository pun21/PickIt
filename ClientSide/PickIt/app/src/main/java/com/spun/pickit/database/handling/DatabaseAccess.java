package com.spun.pickit.database.handling;

import android.util.Log;

import com.spun.pickit.database.handling.crud.PasswordValidation;
import com.spun.pickit.database.handling.crud.PickItCRUD;
import com.spun.pickit.database.handling.crud.UserCRUD;
import com.spun.pickit.database.handling.crud.Following;
import com.spun.pickit.database.handling.crud.ChoiceCRUD;

import com.spun.pickit.model.User;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

public class DatabaseAccess {

    public User validatePassword(String username, String password) {
        PasswordValidation passwordValidation = new PasswordValidation(username,password);
        DataAccess access = new DataAccess(passwordValidation.read());

        JSONObject json = access.getJson();

        User userToSend = this.retrieveUserFromJSON(json);

        return userToSend;
    }

    public int createUser(String username, String password, String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        UserCRUD user = new UserCRUD(username, password, birthday, gender, ethnicity, religion, politicalAffiliation);
        DataAccess access = new DataAccess(user.create());

        JSONObject json = access.getJson();

        int userID = 0;
        try {
            if(json.getInt("Successful") == 1){
                json = json.getJSONObject("Result");
                json = new JSONObject((String)json.get("message"));
                userID = Integer.parseInt((String)json.get("UserID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return userID;
    }

    public int createPickIt(int userID, String category, String subject, String timestamp, String endTime){
        PickItCRUD pickIt = new PickItCRUD(userID, category, subject, timestamp, endTime);
        DataAccess access = new DataAccess(pickIt.create());

        JSONObject json = access.getJson();

        int pickItID = 0;
        try {
            if(json.getInt("Successful") == 1){
                json = json.getJSONObject("Result");
                json = new JSONObject((String)json.get("message"));
                pickItID = Integer.parseInt((String)json.get("UserID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pickItID;
    }

    public User readUser(int userID){
        UserCRUD userCRUDOp = new UserCRUD(userID);
        DataAccess access = new DataAccess(userCRUDOp.read());

        JSONObject json = access.getJson();
        User user = this.retrieveUserFromJSON(json);

        return user;
    }

    public boolean updateUser(int userID, String username,String password,String birthday, String gender, String ethnicity,String religion,String politicalAffiliation){
        UserCRUD userCRUD = new UserCRUD(userID, username,password,birthday,gender,ethnicity,religion,politicalAffiliation);
        DataAccess access = new DataAccess(userCRUD.update());

        JSONObject json = access.getJson();

        return JSONRequestPass(json);
    }


    public boolean createFollowing(String followerID,String leaderID){
        Following following = new Following(followerID,leaderID);
        DataAccess access = new DataAccess(following.create());

        JSONObject json = access.getJson();

        return JSONRequestPass(json);
    }

    public boolean createChoice(int PickItID, String filePath){
        ChoiceCRUD choice = new ChoiceCRUD(PickItID,filePath);
        DataAccess access = new DataAccess(choice.create());

        JSONObject json = access.getJson();

        return JSONRequestPass(json);
    }

    protected User retrieveUserFromJSON(JSONObject json) {
        User user = null;
        try {
            if (JSONRequestPass(json)) {
                json = json.getJSONObject("Result");
                json = new JSONObject((String)json.get("message"));

                int userID = Integer.parseInt((String)json.get("UserID"));
                String username = (String) json.get("Username");

                user = new User(userID, username);
            } else {
                Log.v("readUser", "the json object is not accessed correctly,  try json.get(\"Result\").get([whatever])");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    private boolean JSONRequestPass(JSONObject json){
        boolean pass = false;
        try{
            pass = json.getInt("success") == 1;
        }catch(JSONException e){
            try {
                json = json.getJSONObject("Result");
                pass = json.getInt("success") == 1;
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
                    request = new HttpGet( new URI( url ) );
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
                        json.put("Successful", 0);
                        json.put("Result", "!!!ERROR!!!");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    JSONObject temp = new JSONObject();
                    try{
                        temp.put("Successful", 1);
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