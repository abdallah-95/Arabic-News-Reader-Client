package com.example.abdallah.news_reader;

import android.webkit.JavascriptInterface;

/**
 * Created by Abdallah on 5/29/2017.
 */

public class Javascript {
    public static String msg;
    public static boolean f;
    public static boolean readStoryf;

    @JavascriptInterface
    public String msgFromAndroid(){
        return msg;
    }

    @JavascriptInterface
    public void flag(){
        this.f=false;
    }

    @JavascriptInterface
    public void RSf(){
        this.readStoryf=false;

    }
}
