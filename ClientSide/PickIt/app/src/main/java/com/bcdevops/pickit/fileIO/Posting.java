package com.bcdevops.pickit.fileIO;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Posting {
    final FileUpload fileUploadPreparer;
    final Activity activity;

    public Posting(Activity activity, String fileToUpload, String url, String fileName, boolean deleteAfterwards){
        fileUploadPreparer = new FileUpload(fileToUpload,url, fileName, deleteAfterwards);
        this.activity = activity;
    }

    public void execute(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                fileUploadPreparer.uploadFile();
            }
        }).start();
    }

    class FileUpload {
        final private String fileString;
        final private String urlString;
        final private String fileName;
        final private boolean deleteAfterwards;

        public FileUpload(String fileString, String url, String fileName, boolean deleteAfterwards) {
            this.fileString = fileString;
            this.urlString = url;
            this.fileName = fileName;
            this.deleteAfterwards = deleteAfterwards;
        }

        public int uploadFile() {
            HttpURLConnection conn;
            DataOutputStream dos;
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            int bytesRead, bytesAvailable, bufferSize;
            byte[] buffer;
            int maxBufferSize = 1 * 1024 * 1024;
            String sourceFile = fileString;
            int  serverResponseCode = 0;

            File file = new File(sourceFile);

            if (!file.exists()) {
                try {
                    throw new Exception("File does not exist locally in order to be transmitted to the server");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return 0;
            }
                try {

                    // open a URL connection to the Servlet
                    FileInputStream fileInputStream = new FileInputStream(file);
                    URL url = new URL(urlString);

                    // Open a HTTP  connection to  the URL
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);      // Allow Inputs
                    conn.setDoOutput(true);     // Allow Outputs
                    conn.setUseCaches(false);   // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    conn.setRequestProperty("uploaded_file", fileName);

                    dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                            + fileName + "\"" + lineEnd);

                    dos.writeBytes(lineEnd);

                    // create a buffer of  maximum size
                    bytesAvailable = fileInputStream.available();

                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    buffer = new byte[bufferSize];

                    // read file and write it into form...
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    while (bytesRead > 0) {

                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                    }

                    // send multipart form data necessary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // Responses from the server (code and message)
                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    if(serverResponseCode == 200){

                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                Context context = activity.getApplicationContext();
                                Toast.makeText(context, "File Upload Complete.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //close the streams //
                    fileInputStream.close();
                    dos.flush();
                    dos.close();

                    if(deleteAfterwards){
                        file.delete();
                    }

                } catch (MalformedURLException ex) {
                    ex.printStackTrace();

                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(activity.getApplicationContext(), "MalformedURLException", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return serverResponseCode;
        }
    }
}