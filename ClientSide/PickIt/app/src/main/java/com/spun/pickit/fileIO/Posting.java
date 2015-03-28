package com.spun.pickit.fileIO;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;

public class Posting {

    public Posting(File fileToUpload,String url){
        FileUpload file = new FileUpload(fileToUpload,url);
        file.fileUploader();
    }

    class FileUpload {
        private File file;
        private String url;
        private boolean successful = false;

        public FileUpload(File file, String url) {
            this.file = file;
            this.url = url;
        }

        public void fileUploader() {
            try {
                HttpPost post = new HttpPost(url);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                ContentBody body = new FileBody(file, "json");
                builder.addPart("file", body);

                post.setEntity(builder.build());
                HttpClient client = HttpClientBuilder.create().build();
                HttpResponse response = client.execute(post);

                if (response.getStatusLine().getStatusCode() == 200) {
                    System.out.println("Upload completed");
                    successful = true;
                    System.out.println(successful);
                } else {
                    throw new Exception("upload is not successful");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}