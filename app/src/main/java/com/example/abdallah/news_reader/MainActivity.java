package com.example.abdallah.news_reader;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import android.os.Handler;

import java.util.logging.LogRecord;

import static com.example.abdallah.news_reader.News.news;

public class MainActivity extends AppCompatActivity {

    public static Socket socket = null;
    private final int SPEECH_RECOGNITION_CODE = 1;
    private static String cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/main.html");

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WebView webView = (WebView) findViewById(R.id.webview);
                webView.loadUrl("javascript:speak('"+" اهلا بِكَ فِي تَطْبِيقِ قِرَاءةِ الاخبار"+"');");



            }
        },1000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WebView webView = (WebView) findViewById(R.id.webview);


                webView.loadUrl("javascript:speak('"+"اِخْتَرْ مَنّ بَيْن الْاِخْتِيَارَاتِ التاليه"+"');");


            }
        },7000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                WebView webView = (WebView) findViewById(R.id.webview);


                webView.loadUrl("javascript:speak('"+"المصرى اليوم اقتصاد رياضة اخبار مصر"+"');");


            }
        },12000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                startSpeechToText();


            }
        }, 16500);

    }


    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        // intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        //intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,  RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ar-EG");

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak the news category...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (ActivityNotFoundException a) {

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    final ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    TextView txtview = (TextView) findViewById(R.id.textView);
                    txtview.setText(result.get(0));


                    if (result.get(0).equals("المصري اليوم اقتصاد") ||
                            result.get(0).equals("المصري اليوم رياضه") ||
                            result.get(0).equals("المصري اليوم اخبار مصر") ||
                            result.get(0).equals("مصراوي اقتصاد") ||
                            result.get(0).equals("مصراوي رياضه") ||
                            result.get(0).equals("مصراوي اخبار مصر")) {


                        send(result.get(0));

                    } else
                        startSpeechToText();

                }
            }
            break;
        }
        ;


    }


    public void send(final String s) {
        new Thread() {

            @Override

            public void run() {


                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //stuff that updates ui
                            TextView txtV = (TextView) findViewById(R.id.textView);
                            txtV.setText("attempting");
                        }
                    });

//192.168.1.4 local ip address to the java server
                    socket = new Socket("192.168.1.4", 60123);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //stuff that updates ui
                            TextView txtV = (TextView) findViewById(R.id.textView);
                            txtV.setText("Connected");
                        }
                    });
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    bw.write(s);
                    bw.newLine();
                    bw.flush();

                    ObjectInputStream  ois = new ObjectInputStream(socket.getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //stuff that updates ui
                            TextView txtV = (TextView) findViewById(R.id.textView);
                            txtV.setText("Object stream connected");
                        }
                    });
                    boolean f=true;
                    while(f) {
                        try {
                             String [] data = (String[]) ois.readObject();
                            int x=0;
                            for(int i=0;i<data.length;i++){

                                news.add(x,new News());
                                news.get(x).Headline=data[i];
                                i++;
                                news.get(x).Story=data[i];
                                x++;
                            }
                            if(data.length>0)
                                f=false;
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }

                    }

                    Intent i=new Intent(getApplicationContext(), Read_news.class);
                    startActivity(i);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();

    }


}