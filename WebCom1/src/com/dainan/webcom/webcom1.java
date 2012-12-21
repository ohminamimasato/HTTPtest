package com.dainan.webcom;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class webcom1 extends Activity {
	private TextView mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcom1);
        mView=(TextView)findViewById(R.id.view);
        mView.setText(new String(httpGet(createURL())));
    }

	public String createURL() {
		String apiURL = "http://news.yahooapis.jp/NewsWebService/V2/topics?";
		String appid = "xIKkJiWxg67Q6xX1bxyKFokTS8cEMPsvdG1PCyo6jz5K1yYsC5toV3vqkYP061Q2";
		String category = "top";
		return String.format("%sappid=%s&pickupcategory=%s", apiURL, appid,
				category);
	}

    public static String httpGet(String strURL){
    	//(1)トライキャッチによるエラー処理
    	try{
    		    //(2)URLクラスを使って通信
    		    URL url=new URL(strURL);
    		    URLConnection connection=url.openConnection();
    		   //(3)動作を入力に設定
    		    connection.setDoInput(true);    //データを入力することの宣言
    		    InputStream stream=connection.getInputStream();   //(4)入力ストリームを取得
    		   //(5)得られた入力ストリームをバッファリーダ(input）を使って読み出していく（ための設定）
    		    BufferedReader input=new BufferedReader(new InputStreamReader(stream));
    		   //(6)データの取得処理
    		    String data="";
    		    String tmp="";
    		    		while((tmp=input.readLine()) !=null){     //入力ストリームから1行ずつ読み込み、
    		    			data+=tmp;                                       //dataに次々追加（バッファが空になるまで）
    		    		}
    		   //(7)終了処理
    		     stream.close();
    		     input.close();
    		     return data;         //dataを返却

    	}catch (Exception  e){
    		    //(8)エラー処理
    		return e.toString();
    	    }
    }
}