package com.dainan.webcom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.TextView;

public class webcom1 extends Activity {
	private TextView mView;

	// article
		static private String mArticleTitle[];
		static private String mArticleURL[];
		static private int mArticleNum;

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

	public static String httpGet(String strURL) {
		// (1)try-catchによるエラー処理
		try {
			// (2)URLクラスを使って通信
			URL url = new URL(strURL);
			URLConnection connection = url.openConnection();
			// (3)動作を入力に設定
			connection.setDoInput(true);
			InputStream stream = connection.getInputStream();
			readXML(stream);
			String data="";
			   for(int i=0;i<mArticleNum;i++){
				   data+=mArticleTitle[i];
			   }
			// (5)終了処理
			stream.close();
			return data;
		} catch (Exception e) {
			// (6)エラー処理
			return e.toString();
		}
	}



    public static void readXML(InputStream stream)
			throws XmlPullParserException {
		try {
			XmlPullParser myxmlPullParser = Xml.newPullParser();
			myxmlPullParser.setInput(stream, "UTF-8");

			int cntTitle = 0;
			int cntAddress = 0;
			for (int e = myxmlPullParser.getEventType(); e != XmlPullParser.END_DOCUMENT; e = myxmlPullParser
					.next()) {
				if (e == XmlPullParser.START_TAG) {
					if (myxmlPullParser.getName().equals("ResultSet")) {
						mArticleNum = Integer
								.parseInt(myxmlPullParser.getAttributeValue(
										null, "totalResultsReturned"));
						mArticleTitle = new String[mArticleNum];
						mArticleURL = new String[mArticleNum];
					} else if (myxmlPullParser.getName().equals("Title")) {
						mArticleTitle[cntTitle] = myxmlPullParser.nextText();
						cntTitle++;
					} else if (myxmlPullParser.getName()
							.equals("SmartphoneUrl")) {
						mArticleURL[cntAddress] = myxmlPullParser.nextText();
						cntAddress++;
					}
				}

			}
		} catch (XmlPullParserException e) {
		} catch (IOException e) {
		}

	}

}