package com.spun.pickit.database.handling;

import android.util.Log;

import com.spun.pickit.database.handling.crud.PasswordValidation;
<<<<<<< HEAD
import com.spun.pickit.database.handling.crud.UserCRUD;
import com.spun.pickit.database.handling.crud.Following;
import com.spun.pickit.database.handling.crud.ChoiceCRUD;

import com.spun.pickit.model.User;

=======
import com.spun.pickit.database.handling.crud.User;
>>>>>>> origin/bensBranch

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

public class DatabaseAccess {
    public boolean validatePassword(String username, String password) {
        PasswordValidation passwordValidation = new PasswordValidation(username,password);
        DataAccess access = new DataAccess(passwordValidation.read());

        JSONObject json = access.getJson();

        boolean pass = false;
        try {
            if(json.get("Successful")==1){
                json = json.getJSONObject("Result");
                pass = json.get("success") == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pass;
    }

    public boolean saveUserProfile(String username, String password, String birthday, String gender, String ethnicity, String religion, String politicalAffiliation){
        User user = new User(username, password, birthday, gender, ethnicity, religion, politicalAffiliation);
        DataAccess access = new DataAccess(user.create());

        JSONObject json = access.getJson();

        boolean pass = false;

        try {
            if(json.get("Successful") == 1){
                json = json.getJSONObject("Result");
                pass = json.get("success") == 1;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return pass;
    }












    public User readUser(String userID){
        UserCRUD userCRUDOp = new UserCRUD(userID);
        DataAccess access = new DataAccess(userCRUDOp.read());

        JSONObject json = access.getJson();
        User userToSend = null;
        try {
            if (this.JSONResquestPass(json)) {
                userToSend = new User((int)json.get("UserID"), (String)json.get("Username"),(String) json.get("Gender"), (String)json.get("Religion"), (String)json.get("PoliticalAffiliation"), (String)json.get("Birthday"), (String)json.get("Ethnicity"));
            } else {
               Log.v("readUsesr", "the json object is not accesed correctly,  try json.get(\"Result\").get([whatever])");
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
        return userToSend;
    }

    public boolean updateUser(String username,String password,String birthday, String gender, String ethnicity,String religion,String politicalAffiliation){
        UserCRUD userCRUDOp = new UserCRUD(username,password,birthday,gender,ethnicity,religion,politicalAffiliation);
        DataAccess access = new DataAccess(userCRUDOp.update());

        JSONObject json = access.getJson();
        return this.JSONResquestPass(json);
    }


    public boolean createFollowing(String followerID,String leaderID){
        Following following = new Following(followerID,leaderID);
        DataAccess access = new DataAccess(following.create());

        JSONObject json = access.getJson();
        return this.JSONResquestPass(json);
    }

    public boolean createChoice(String PickitID, String filePath){
        ChoiceCRUD choice = new ChoiceCRUD(PickitID,filePath);
        DataAccess access = new DataAccess(choice.create());

        JSONObject json = access.getJson();
        return this.JSONResquestPass(json);
    }
    
    public boolean JSONResquestPass(JSONObject json){
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
