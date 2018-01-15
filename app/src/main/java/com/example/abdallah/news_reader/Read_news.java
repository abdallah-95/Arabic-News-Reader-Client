package com.example.abdallah.news_reader;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

public class Read_news extends AppCompatActivity {

    Javascript js = new Javascript();
    public static int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_news);


        WebView webView = (WebView) findViewById(R.id.webview2);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.setWebChromeClient(new WebChromeClient());

        webView.addJavascriptInterface(js,"js");
        webView.loadUrl("file:///android_asset/read_news.html");


        final Handler handler=new Handler();
        final Button b =(Button) findViewById(R.id.open);

        Thread newTread = new Thread() {
            Integer n=new Integer(0);


            @Override
            public void run() {


                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        WebView webView = (WebView) findViewById(R.id.webview2);

                        webView.loadUrl("javascript:responsiveVoice.pause();");
                        js.readStoryf=true;
                        webView.loadUrl("javascript:speak2('" + News.news.get(x).Story + "');");

                    }
                });

                do {
                    js.f = true;
                     n+=5000;
                    final String s = News.news.get(x).Headline;

                    handler.postDelayed(new Runnable() {

                        public void run() {
                            WebView webView = (WebView) findViewById(R.id.webview2);
                            webView.loadUrl("javascript:speak('" + s + "');");

                        }
                    }, n );
                    while(js.f || js.readStoryf) {
                        try {
                            Thread.sleep(10000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(x<(News.news.size()))
                        x++;
                    /*runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            //stuff that updates ui
                            TextView txtview =(TextView)findViewById(R.id.textView);
                            txtview.setText("Changed!");
                        }
                    });*/

                }while(x<(News.news.size()));
            }
        };
        newTread.start();

    }
}
