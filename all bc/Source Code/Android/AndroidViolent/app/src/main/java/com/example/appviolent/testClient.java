package com.example.appviolent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class testClient extends AsyncTask<String, Bitmap, String> {

    @Override
    protected void onProgressUpdate(Bitmap... values) {
        super.onProgressUpdate(values);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected String doInBackground(String... params) {

        Log.i("hola","Running");
        try {
            boolean XD = true;
            byte conteo = 0;
            while(XD){
                Socket socket = new Socket("26.182.225.9",9999);
                conteo = 0;
                BufferedReader mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));                String xxx = mBufferIn.readLine();

                int size = Integer.parseInt(xxx);                String next = mBufferIn.readLine();

                InputStream targetStream = new ByteArrayInputStream(next.getBytes());
                System.out.println(size);
                System.out.println(targetStream+"9990");
//                Log.i("Printing image",next.length()+" ::: " + size + " ::: " + targetStream.available());

//                Log.i("substring", next.substring(size,next.length()));
//                String newNext = next.substring(0,size);
//                int maxLogSize = 1000;
                //showing the base64 string

//                for(int i = 0; i <= next.length() / maxLogSize; i++) {
//                    int start = i * maxLogSize;
//                    int end = (i+1) * maxLogSize;
//                    end = end > next.length() ? next.length() : end;
//                    Log.v("OOOOOO", next.substring(start, end));
//                }
//
//
//                byte[] decodedString = Base64.decode(next, Base64.DEFAULT);
//                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                this.publishProgress(decodedByte);
//                String happyFace = mBufferIn.readLine();
//                Log.i("Happy face?: ",happyFace);
//                if(targetStream == null)
//                    XD = false;

//            } catch (IOException e) {
//                e.printStackTrace();

            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
        //setting the bitmap to imageView
    }

}
