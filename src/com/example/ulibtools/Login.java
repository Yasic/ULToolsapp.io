package com.example.ulibtools;

import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

@SuppressLint("NewApi") public class Login extends Activity{
	private String flag,name,date,college,ids,password,url;
	private Button booklistbutton;
	public ListView listview;
	public ArrayList<HashMap<String, String>> mylist;
	public TextView username,usercollege,userdate,testtext;
	public SimpleAdapter mSchedule;
	private AsyncHttpClient clienta;
	private RequestParams paramss;
	public int position,positions;
	public ArrayList<String> bookhref,namelist,valuelist; 
	public android.app.ActionBar actionBar;
	
	@Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);  
        setContentView(R.layout.listview);
        actionBar = getActionBar();  
        
        
        testtext = (TextView)findViewById(R.id.testtext);//测试用的地方
		booklistbutton = (Button)findViewById(R.id.button2);
		this.booklistbutton.setEnabled(false);
        
		Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("key");
        ids = bundle.getString("extpatid");
        password = bundle.getString("extpatpw");
        url = bundle.getString("firsturl");
        //testtext.setText(url);
        org.jsoup.nodes.Document doc = Jsoup.parse(url);
        Elements href = doc.select("a[href~=items]");
    	String linkHref = href.attr("href");
    	flag = "https://webpac.uestc.edu.cn"+linkHref;
    	
    	
    	Elements info = doc.getElementsByClass("patNameAddress");
    	String name = info.select("strong").text();
    	
    	String s = info.toString();
    	String[] sArray=s.split("<br>"); 
    	String a = "";
    	for (int i=0;i<sArray.length;i++) {  
    	    a = a + sArray[i] + '\n';
    	}
    	
    	clienta = new AsyncHttpClient();
    	paramss = new RequestParams();
    	
        username = (TextView)findViewById(R.id.username);
        username.setText(name);
        usercollege = (TextView)findViewById(R.id.usercollege);
        usercollege.setText(sArray[1]);
        userdate = (TextView)findViewById(R.id.userdate);
        userdate.setText(sArray[2].substring(7));
        
        mylist = new ArrayList<HashMap<String, String>>(); 
        paramss.put("extpatid",ids);
        paramss.put("extpatpw", password);
        paramss.put("submit", "submit");
        paramss.put("submit.x","0");
        paramss.put("submit.y","0");
        
    	clienta.post(flag, paramss, new AsyncHttpResponseHandler() {            
            public void onSuccess(String response) {
            	String html = response.toString();
            	
            	org.jsoup.nodes.Document docs = Jsoup.parse(html);
            	Elements links = docs.select("a[href~=record]");
            	Elements dates = docs.getElementsByClass("patFuncStatus");
            	Elements bookmark = docs.select("input[type~=checkbox]");
            	namelist = new ArrayList<String>(); //ArrayList不实例化就不可用，程序会异常跳出
            	valuelist = new ArrayList<String>();
            	for(org.jsoup.nodes.Element link:bookmark){
            		namelist.add(link.attr("name"));//获取到所有name值
            		valuelist.add(link.attr("value"));//获取到所有name值
            	}
            	
            	ArrayList dateslist = new ArrayList();
            	for(org.jsoup.nodes.Element link:dates){
            		dateslist.add(link.text());
            	}
            	
            	int i=1;//下面需要用上
            	String linkHref = "";
            	bookhref = new ArrayList<String>();
            	for(org.jsoup.nodes.Element link:links) {
            		HashMap<String, String> map = new HashMap<String, String>(); 
            		linkHref = link.attr("href") ;
            		bookhref.add(linkHref);
            		String[] bookname = link.toString().split(" ");
            		map.put("name",bookname[2]);
            		map.put("date", dateslist.get(i-1).toString());
            		mylist.add(map);
            		i++;
            	}
            	//testtext.
            	listview = (ListView)findViewById(R.id.listview1);
            	positions = 0;
            	listview.setOnItemClickListener(new OnItemClickListener(){
            		@Override
            		public void onItemClick(AdapterView<?> parent, View view,
            		  int position, long id) {
            			positions = position;
            			
            	        actionBar.hide(); 
            	        setContentView(R.layout.loginloading);
            			String href = "https://webpac.uestc.edu.cn" + bookhref.get(position);
            			AsyncHttpClient client = new AsyncHttpClient();
            	    	client.get(href, null, new AsyncHttpResponseHandler() {            
            	            public void onSuccess(String response) {
            	            	String html = response;
            	            	
            	            	org.jsoup.nodes.Document doc = Jsoup.parse(html);
            	            	Elements bookinfokey = doc.getElementsByClass("bibInfoLabel");
            	            	Elements bookinfovalue = doc.getElementsByClass("bibInfoData");
            	            	String bookinfo = "";
            	            	bookinfo = bookinfokey.get(0).text() + ":" + bookinfovalue.get(0).text() + "\n\n" 
            	            			+ bookinfokey.get(2).text() + ":" + bookinfovalue.get(2).text() + "\n\n"
            	            			+ bookinfokey.get(4).text() + ":" + bookinfovalue.get(4).text() + "\n\n"
            	            			+ bookinfokey.get(5).text() + ":" + bookinfovalue.get(5).text() ;
                    			Intent intent = new Intent(Login.this,bookinfo.class);
                    			Bundle bundle = new Bundle();
                    			bundle.putString("bookinfo", bookinfo);
                    			bundle.putString("bookname", namelist.get(positions+1));
                    			bundle.putString("bookvalue", valuelist.get(positions+1));
                    			//bundle.putInt("renew", positions-1);//position从0开始！
                    			bundle.putString("extpatid", ids);
                    	    	bundle.putString("extpatpw",password);
                    	    	bundle.putString("pageurl",flag);
                    	    	bundle.putString("firsturl", url);
                    	    	bundle.putInt("renew", positions);
                    			intent.putExtra("skey",bundle);
                    			finish();//必须finish
                    			startActivity(intent);
            	            }
            	            });
            			
            		}
            	});
    			mSchedule = new SimpleAdapter(
    					Login.this,
    					mylist,
    					R.layout.listviewitem,      
    					new String[]{"name","date"}, 
    					new int[] {R.id.name,R.id.date}); 
    			booklistbutton.setEnabled(true);
    			    	
    }          
});
    	
    	booklistbutton.setOnClickListener(new OnClickListener(){
    		@Override
    		public void onClick(View v){
    			booklistbutton.setEnabled(false); 
    			listview.setAdapter(mSchedule); 
    		}
            //onclick
    	});//button
    	
    	}
	/*
	@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event)  
    {  
        if(keyCode == KeyEvent.KEYCODE_BACK){  
            Intent myIntent = new Intent();  
            myIntent = new Intent(Login.this, MainActivity.class);  
            startActivity(myIntent);  
            this.finish();  
        }  
        return super.onKeyDown(keyCode, event);  
    }  
	*/
	@Override
	public void finish(){
		super.finish();
	}
	@Override
	public void onPause(){
		super.onPause();
		//finish();
	}
	@Override
	public void onRestart(){
		super.onRestart();
	}
	@Override  
	public boolean onCreateOptionsMenu(Menu menu) {  
	    MenuInflater inflater = getMenuInflater();  
	    inflater.inflate(R.menu.main, menu);  
	    return super.onCreateOptionsMenu(menu);  
	}  
	
	@Override  
	public boolean onOptionsItemSelected(MenuItem item) {  
	    switch (item.getItemId()) {  
	    case R.id.action_button0:  
	    	Intent intent = new Intent(Login.this,Login.class);
        	Bundle bundle = new Bundle();
        	bundle.putString("extpatid", ids);
        	bundle.putString("extpatpw",password);
        	bundle.putString("firsturl", url);
        	intent.putExtra("key",bundle);
        	finish();
        	startActivity(intent);//给this发送intent实现刷新功能
	        return true;  
	    case R.id.action_button1:  
	    	Intent intento = new Intent(Login.this,MainActivity.class);
	    	finish();
	    	startActivity(intento);
	        return true;  
	    case R.id.action_button2:  
	        onPause();  
	        return true;  
	    default:  
	        return super.onOptionsItemSelected(item);  
	    }  
	}
}
