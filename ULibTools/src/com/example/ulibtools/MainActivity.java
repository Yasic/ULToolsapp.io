package com.example.ulibtools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.ByteArrayBuffer;
import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.Element;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import android.support.v7.app.ActionBarActivity;
import android.text.method.ScrollingMovementMethod;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract.Document;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity  {
    	private WebView webView;
    	public Button button,backbutton;
    	public String flag,id,password;
    	public EditText edit1;
    	public EditText edit2; 
    	public ListView listview;
    	public TextView username,usercollege,testtext,userdate;
    	public SharedPreferences sharedPreferences;  
        public SharedPreferences.Editor editor;  
    	public ArrayList<HashMap<String, String>> mylist; 
    	public static MainActivity instance = null;
        @Override  
        protected void onCreate(Bundle savedInstanceState) { 
        	super.onCreate(savedInstanceState);  
        	this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        	setContentView(R.layout.activity_main);
        	instance = this;
        	sharedPreferences = this.getSharedPreferences("test",MODE_PRIVATE);  
            editor = sharedPreferences.edit(); 
            
            button = (Button)findViewById(R.id.button1);
            button.setOnClickListener(new OnClickListener() {  
                @Override  
                public void onClick(View v) {
                	
                	edit1 = (EditText)findViewById(R.id.editview1);
                    edit2 = (EditText)findViewById(R.id.editview2);
                	if(edit1.length() == 0 || edit2.length() ==0){
                		Toast.makeText(getApplicationContext(), "用户名或密码不能为空！",
               			     Toast.LENGTH_SHORT).show();
                		return;
                	}
                	button.setEnabled(false);
                    String url = "https://webpac.uestc.edu.cn/patroninfo*chx";
                    RequestParams params = new RequestParams();
                    params.put("extpatid",edit1.getText().toString());
                    params.put("extpatpw", edit2.getText().toString());
                    params.put("submit", "submit");
                    params.put("submit.x","0");
                    params.put("submit.y","0");
                    edit1 = (EditText)findViewById(R.id.editview1);
                    edit2 = (EditText)findViewById(R.id.editview2);
                    id = edit1.getText().toString();
                    password = edit2.getText().toString();
                    
                    setContentView(R.layout.loginloading);
                    
                    AsyncHttpClient client = new AsyncHttpClient();
                	client.post(url, params, new AsyncHttpResponseHandler() {            
                        public void onSuccess(String response) {
                        	
                        	flag = response.toString();
                        	String url = response.toString();
                        	
                        	org.jsoup.nodes.Document doc = Jsoup.parse(flag);
                        	Elements test = doc.select("img[src~=2007Logo.png]");
                        	String test1 = test.attr("src");
                        	if(test1.equals("/screens/wpp2007Logo.png")){
                        		Toast.makeText(getApplicationContext(), "登陆成功！",
                        			     Toast.LENGTH_SHORT).show();
                        	}
                        	else{
                        		Toast.makeText(getApplicationContext(), "登陆失败,请检查密码输入",
                        			     Toast.LENGTH_SHORT).show();
                        		return;
                        	}
                        	
                        	Intent intent = new Intent(MainActivity.this,Login.class);
                        	Bundle bundle = new Bundle();
                        	bundle.putString("extpatid", id);
                        	bundle.putString("extpatpw",password);
                        	bundle.putString("firsturl", url);
                        	intent.putExtra("key",bundle);
                        	finish();
                        	startActivity(intent);
                          }
                    }
                	);
        }});
            
        }

    	@Override
    	public void onPause(){
    		super.onPause();
    		//MainActivity.instance.finish();
    	}
    	
    	@Override  
        public boolean onKeyDown(int keyCode, KeyEvent event)  
        {  
            if(keyCode == KeyEvent.KEYCODE_BACK){   
                this.finish();  
            }  
            return super.onKeyDown(keyCode, event);  
        }  
    	
    	@Override
    	public void finish(){
    		super.finish();
    	}
}


