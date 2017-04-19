package com.example.lg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.*;
import java.net.*;

//Referenced classes of package com.example.login:
//         LoginoutActivity

public class LoginActivity extends Activity
{

	 public Button btn_signinButton;
	 public EditText et_name;
	 public EditText et_pwd;
	 public String nameString=null;
	 public String pwdString=null;
 protected static final int LOING = 1;
protected static final int LOINGERROR = 0;


//1 主线程创建消息处理器
private final  Handler handler = new Handler(){
	 public void handleMessage(android.os.Message msg) {
		 if (msg.what == LOING) {
			Toast.makeText(LoginActivity.this, "登录成功",0).show();
			 Intent intent_outIntent = new Intent();
             intent_outIntent.setClass(LoginActivity.this, LoginoutActivity.class);
             startActivity(intent_outIntent);
             finish();
		}
		 if (msg.what == LOINGERROR) {
				Toast.makeText(LoginActivity.this, "登录失败",0).show();
		 	
		 }
	 };
};

 protected void onCreate(Bundle savedInstanceState)
 {
     super.onCreate(savedInstanceState);
     requestWindowFeature(Window.FEATURE_NO_TITLE);
     setContentView(R.layout.activity_in);
     btn_signinButton = (Button)findViewById(R.id.signin_button);
     et_name = (EditText)findViewById(R.id.username_edit);
     et_pwd = (EditText)findViewById(R.id.password_edit);
     
     SharedPreferences sharedPreferences=getSharedPreferences("user",Context.MODE_PRIVATE);
     String nameValue = sharedPreferences.getString("name", "");
     String pwdValue = sharedPreferences.getString("pwd", "");
     et_name.setText(nameValue);
     et_pwd.setText(pwdValue);
     
     btn_signinButton.setOnClickListener(new android.view.View.OnClickListener() {

         public void onClick(View v)
         {
             nameString = et_name.getText().toString();
             pwdString = et_pwd.getText().toString();
             SharedPreferences preferences=getSharedPreferences("user",Context.MODE_PRIVATE);
        	 Editor editor=preferences.edit();
        	 
        	 editor.putString("name", nameString);
        	 editor.putString("pwd", pwdString);
        	 editor.commit();
             if(nameString.equals("") ){
            	 Toast.makeText(LoginActivity.this, "请您先输入用户名",0).show();
             }
             if(pwdString.equals("")){
            	 Toast.makeText(LoginActivity.this, "请您先输入密码",0).show();
             }
             if(isWifiConnected(LoginActivity.this) !=true) {
            	 Toast.makeText(LoginActivity.this, "请您先连接校园WiFi：web.wlan.bjtu",0).show();
			}
             
             if(isWifiConnected(LoginActivity.this) ==true&&!nameString.equals("")&&!pwdString.equals("")){
            	
            	 Login(nameString, pwdString);
             }
            
             
         }

       
     }
);
 }

 public boolean isWifiConnected(Context context) {    
	    if (context != null) {    
	        ConnectivityManager mConnectivityManager = (ConnectivityManager) context    
	                .getSystemService(Context.CONNECTIVITY_SERVICE);    
	        NetworkInfo mWiFiNetworkInfo = mConnectivityManager    
	                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);    
	        if (mWiFiNetworkInfo != null) {    
	            return mWiFiNetworkInfo.isAvailable();    
	        }    
	    }    
	    return false;    
	}  
 public void Login(final String name, final String pwd)
 {
     Thread t = new Thread(new Runnable() {

         public void run()
         {
             try
             {
                 URL postUrl = new URL("http://10.1.61.1/a70.htm");
                 HttpURLConnection connection = (HttpURLConnection)postUrl.openConnection();
                 connection.setDoOutput(true);
                 connection.setDoInput(true);
                 connection.setRequestMethod("POST");
                 connection.setUseCaches(false);
                 connection.setInstanceFollowRedirects(true);
                 connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                 connection.connect();
                 DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                 String content = (new StringBuilder("DDDDD=")).append(URLEncoder.encode(name, "UTF-8")).toString();
                 content = (new StringBuilder(String.valueOf(content))).append("&upass=").append(URLEncoder.encode(pwd, "UTF-8")).toString();
                 content = (new StringBuilder(String.valueOf(content))).append("&0MKKey=%B5%C7%C2%BC%28Login%29").toString();
                 content = (new StringBuilder(String.valueOf(content))).append("&C2=on").toString();
                 out.writeBytes(content);
                 out.flush();
                 out.close();
                 connection.getInputStream();
                 
                 connection.disconnect();
                 int code = connection.getResponseCode();
                 System.out.println("in"+code);
                 if(code == 203 || code ==200){
                	 //跳转页面
                	
                	 //告诉主线程改UI
                	 Message msg = new Message();
                	 msg.what = LOING;
                	 handler.sendMessage(msg);
                 }else {
                	 //告诉主线程改UI
                	 Message msg = new Message();
                	 msg.what = LOINGERROR;
                	 handler.sendMessage(msg);
				}
                	
                 
             }
             catch(MalformedURLException e)
             {
                 e.printStackTrace();
             }
             catch(ProtocolException e)
             {
                 e.printStackTrace();
             }
             catch(UnsupportedEncodingException e)
             {
                 e.printStackTrace();
             }
             catch(IOException e)
             {
                 e.printStackTrace();
             }
         }

        

         
        
     }
);
     t.start();
 }


}

