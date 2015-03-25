package com.spun.pickit.fileIO;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.HttpResponse;


//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.entity.mime.content.FileBody;
//import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class ServerFileManager {
    final private String url = "http://www.bcdev.me:8080/upload_file.php";
    final private String filePath;

    public ServerFileManager(String filePath){
        this.filePath = filePath;
    }

//    public void uploadFile(){
//        PictureUpload upload = new PictureUpload();
//    }
//
//    class PictureUpload{
//        public PictureUpload(){
//            new AsyncFileUploaderThread().start();
//        }
//
//        class AsyncFileUploaderThread extends Thread {
//            @Override
//            public void run() {
//                HttpClient client = HttpClientBuilder.create().build();
//
//                try {
//                    HttpPost post = new HttpPost(url);
//                    FileBody fileB = new FileBody(new File(filePath));
//                    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//                    builder.addPart("file", fileB);
//                    post.setEntity(builder.build());
//                    HttpResponse response = client.execute(post);
//
//                    if (!(response.getStatusLine().getStatusCode() == 200))
//                        throw new Exception("Unable to save file.");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
   // }
}

