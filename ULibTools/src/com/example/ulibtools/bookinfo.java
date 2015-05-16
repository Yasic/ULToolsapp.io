package com.example.ulibtools;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class bookinfo extends Activity {
	public String href,id,password,firsturl,bookname,bookvalue,pageurl;
	public int position;
	public TextView bookpage;
	public Button button,backbutton;
	String bookinfo;
	public String html;
	public Elements bookinfokey ;
    public Elements bookinfovalue;
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.bookpage);
        
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("skey");
        href = "https://webpac.uestc.edu.cn" + bundle.getString("href");
        id = bundle.getString("extpatid");
        password = bundle.getString("extpatpw");
        pageurl = bundle.getString("pageurl");
        firsturl = bundle.getString("firsturl");
        bookinfo = bundle.getString("bookinfo");
        bookname = bundle.getString("bookname");
        bookvalue = bundle.getString("bookvalue");
        position = bundle.getInt("renew");
        bookpage = (TextView)findViewById(R.id.bookpage);
        bookpage.setText(bookinfo);
        button = (Button)findViewById(R.id.xujiebutton);
        button.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		button.setEnabled(false);
        		RequestParams params1 = new RequestParams();
        		params1.put("extpatid",id);
                params1.put("extpatpw", password);
                params1.put("submit", "submit");
                params1.put("submit.x","0");
                params1.put("submit.y","0");
        		AsyncHttpClient client = new AsyncHttpClient();
        		//��һ�β���ʱ�䣺2015/5/16/0:11 ʧ�ܣ�ԭ��²���post���params��renewsome����
        		//��ʵ��û��cookies
        		//�ڶ��β���ʱ�䣺2015/5/16/10:44ʧ�ܣ�ԭ��²⻹��renewsome����
        		//post�����ж���һЩ����	params2.put("requestRenewSome","requestRenewSome");
        		//�����β���ʱ�䣺2015/5/16/10:52ʧ�ܣ�ԭ�������鿴html�ļ�
        		//��ʵ��ʵ���ˡ�ֻ��������Ҫʱ����ϣ�����һֱʧ��
        		PersistentCookieStore myCookieStore = new PersistentCookieStore(getApplicationContext());  
        		client.setCookieStore(myCookieStore);  //����cookies
        		client.post("https://webpac.uestc.edu.cn/patroninfo*chx", params1,new AsyncHttpResponseHandler() {            
                    public void onSuccess(String response) {
                    	RequestParams params2 = new RequestParams();
                		params2.put("currentsortorder","current_checkout");
                		params2.put(bookname, bookvalue);
                		params2.put("currentsortorder", "current_checkout");
                		params2.put("renewsome", "��");
                		
                    	AsyncHttpClient clients = new AsyncHttpClient();
                    	PersistentCookieStore myCookieStore = new PersistentCookieStore(getApplicationContext());
                    	clients.setCookieStore(myCookieStore);
                    	clients.post(pageurl, params2, new AsyncHttpResponseHandler(){
                    		public void onSuccess(String response){
                    			org.jsoup.nodes.Document docs = Jsoup.parse(response);
                    			Elements date = docs.getElementsByClass("patFuncStatus");
                    			bookpage.setText(date.get(position).text());//��ȡ�����践�ص���Ϣ
                    		}
                    	});
                    }});
        	}
        });
        backbutton = (Button)findViewById(R.id.backbutton1);
        backbutton.setOnClickListener(new OnClickListener(){
        	@Override
        	public void onClick(View v){
        		backbutton.setEnabled(false);
        		onPause();
        	}
        });
        }
	
	@Override
	public void onPause(){
		super.onPause();
		Intent intent = new Intent(bookinfo.this,Login.class);
    	Bundle bundle = new Bundle();
    	bundle.putString("extpatid", id);
    	bundle.putString("extpatpw",password);
    	bundle.putString("firsturl", firsturl);
    	intent.putExtra("key",bundle);
    	finish();
    	startActivity(intent);
    	//MainActivity.instance.finish();
	}

	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {  
	    //MenuInflater inflater = getMenuInflater();  
	    //inflater.inflate(R.menu.main, menu);  
	    return super.onCreateOptionsMenu(menu);  
	}  
	
	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	    case R.id.action_button0:  
	    	Intent intent = new Intent(bookinfo.this,bookinfo.class);
        	Bundle bundle = new Bundle();
        	bundle.putString("extpatid", id);
        	bundle.putString("extpatpw",password);
        	bundle.putString("firsturl", firsturl);
        	intent.putExtra("key",bundle);
        	finish();
        	startActivity(intent);//��this����intentʵ��ˢ�¹���
	        return true;  
	    case R.id.action_button1:  
	    	Intent intento = new Intent(bookinfo.this,MainActivity.class);
	    	finish();
	    	startActivity(intento);
	        return true;  
	    case R.id.action_button2:  
	    	
	        return true;  
	    default:  
	        return super.onOptionsItemSelected(item);  
	    }  
	} 
}
