package com.example.appviolent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.Menu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityDemoSocket extends AppCompatActivity {

    Button button;
    TextView textView;

    private Socket clientSocket;
    private BufferedReader socketIn;
    private PrintWriter socketOut;
    private int port = 9999;
    private final String ip = "26.182.225.9";
    private MyHandler myHandler;
    private MyThread myThread;
    private static final int BUFFER_SIZE = 65536    ;
    static String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo_socket);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            clientSocket = new Socket(ip, port);
            socketIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()) , BUFFER_SIZE);
            socketOut = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView3);
        myHandler = new MyHandler();
        myThread = new MyThread();
        myThread.start();
    }

    public void btn_socket(View v) {


    }

    class MyThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    System.out.println("Trying to read...");
////                    System.out.println(socketIn.readLine() == null);
//                    imageBytes = Convert.FromBase64String(base64String);
                    String data = socketIn.readLine();
                    System.out.println(data);
                    Message msg = myHandler.obtainMessage();
                    msg.obj = data;
                    myHandler.sendMessage(msg);
                    System.out.println(data);
                    socketOut.print("Try" + "\r\n");
                    socketOut.flush();
                    System.out.println("Message sent");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    class MyHandler extends Handler {


        public void handleMessage(Message msg) {
//              System.out.println(msg.obj.getClass() + "asabdsad");
            textView.setText(msg.obj.toString());
        }
    }
}