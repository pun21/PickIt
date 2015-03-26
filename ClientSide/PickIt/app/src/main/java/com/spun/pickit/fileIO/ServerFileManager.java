package com.spun.pickit.fileIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;

public class ServerFileManager {
    final private String UPLOAD_DEMO_FILE = "upload_demographics.php";
    final private String UPLOAD_PICTURE = "upload_picture.php";
    private String url = "http://www.bcdev.me:8080/";
    private File file;

    public ServerFileManager(File file){
        this.file = file;
    }

    public void uploadPicture(){
        //todo
    }

    public void uploadDemographics(){
        url += UPLOAD_DEMO_FILE;
        new AsyncFileUploaderThread().start();
    }


    class AsyncFileUploaderThread extends Thread {
        @Override
        public void run() {
            HttpClient client = HttpClientBuilder.create().build();

            try {
                HttpPost post = new HttpPost(url);
                FileBody fileB = new FileBody(file);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.addPart("file", fileB);
                post.setEntity(builder.build());
                HttpResponse response = client.execute(post);

                if (!(response.getStatusLine().getStatusCode() == 200))
                    throw new Exception("Unable to save file.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

