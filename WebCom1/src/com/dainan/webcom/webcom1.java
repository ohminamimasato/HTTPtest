package com.dainan.webcom;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
        ListView list =(ListView)findViewById(R.id.ListView01);
        ArrayList<ListItem> arrayList=new ArrayList<ListItem>();
        for(int i=0;i<mArticleNum;i++){
        	   arrayList.add(new ListItem(mArticleTitle[i],mArticleURL[i]));
        }
        list.setAdapter(new ListArrayAdapter(this,arrayList));
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

    public class ListItem{
    	   public String title;
    	   public String url;

    	   public ListItem(String title,String url){
    		        this.title=title;
    		        this.url=url;
    	   }
    }

    public class ListArrayAdapter extends ArrayAdapter<ListItem> implements
                     View.OnClickListener{
    	private ArrayList<ListItem> listItem;

    	public ListArrayAdapter(Context context,ArrayList<ListItem> listItem){
    		       super(context,R.layout.rowitem,listItem);
    		       this.listItem=listItem;
    	}

    	@Override
    	public View getView(int position,View view,ViewGroup parent){
    		//（１）レイアウトの指定
    		ListItem item =listItem.get(position);
    		Context context=getContext();
    		LinearLayout linearLayout=new LinearLayout(context);
    		view=linearLayout;
    		TextView textView=new TextView(context);
    		textView.setText(item.title);
    		linearLayout.addView(textView);
    		Button button=new Button(context);
    		button.setText("GO");
    		button.setTag(String.valueOf(position));
    		button.setOnClickListener(this);
    		linearLayout.addView(button,0);
    		return view;
    	}

    	public void onClick(View view){
    		//（２）クリックによる動作
    		int tag=Integer.parseInt((String) view.getTag());
    		ListItem item=listItem.get(tag);
    		try{
    			   Intent intent=new Intent("android.intent.action.VIEW",Uri
    					                  .parse(item.url));
    			   startActivity(intent);
    		}catch(Exception e){
    			e.printStackTrace();
    		}
    	}

    }

}