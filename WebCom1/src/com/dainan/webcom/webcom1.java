package com.dainan.webcom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Xml;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class webcom1 extends Activity {
	private TextView mView;

	// article
		static private String mArticleTitle[];
		static private String mArticleURL[];
		static private int mArticleNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webcom1);
        //
        getArticle(createURL());
        //
        ListView list =(ListView)findViewById(R.id.ListView01);
        //
        list.setAdapter(new ArrayAdapter<String>(this,R.layout.rowitem,mArticleTitle));
    }


	public String createURL() {
		String apiURL = "http://news.yahooapis.jp/NewsWebService/V2/topics?";
		String appid = "xIKkJiWxg67Q6xX1bxyKFokTS8cEMPsvdG1PCyo6jz5K1yYsC5toV3vqkYP061Q2";
		String category = "top";
		return String.format("%sappid=%s&pickupcategory=%s", apiURL, appid,
				category);
	}

	public static void getArticle(String strURL){
		try {
			URL url =new URL(strURL);
			URLConnection connection = url.openConnection();
			connection.setDoInput(true);
			InputStream stream = connection.getInputStream();
			readXML(stream);
			stream.close();
		}catch (Exception e) {
			e.printStackTrace();
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