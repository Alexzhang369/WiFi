package com.example.lg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.net.*;

import org.apache.http.cookie.Cookie;

//Referenced classes of package com.example.login:
//         LoginActivity

public class LoginoutActivity extends Activity {
	public Button btn_signoutButton;
	private WifiManager wifiManager;
	protected static final int LOINGOUT = 2;

	// 1 主线程创建消息处理器
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == LOINGOUT) {
				wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

				wifiManager.setWifiEnabled(false);
				Toast.makeText(LoginoutActivity.this, "注销成功", 0).show();
			}
		};
	};

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_out);
		btn_signoutButton = (Button) findViewById(R.id.signout_button);

		btn_signoutButton
				.setOnClickListener(new android.view.View.OnClickListener() {

					public void onClick(View v) {
						Loginout();
						Intent intent_inIntent = new Intent();
						intent_inIntent.setClass(LoginoutActivity.this,
								LoginActivity.class);
						startActivity(intent_inIntent);
						finish();
					}

				});
	}
	
	/*@Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            moveTaskToBack(false);  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }  */

	public void Loginout() {
		Thread t = new Thread(new Runnable() {

			public void run() {
				try {
					URL postUrl = new URL("http://10.1.61.1/F.htm");
					HttpURLConnection connection = (HttpURLConnection) postUrl
							.openConnection();
					connection.setDoOutput(true);
					connection.setDoInput(true);
					connection.setRequestMethod("GET");
					connection.setUseCaches(false);
					connection.setInstanceFollowRedirects(true);
					connection
							.setRequestProperty("Cookie",
									"PHPSESSID=tpgr6a3m514m11j6tv3v2r6pm7; ip=172.27.166.113; UID=14120365");

					connection.setRequestProperty("Content-Type",
							"application/x-www-form-urlencoded");
					connection.connect();

					int code = connection.getResponseCode();

					System.out.println("out" + code);

					if (code == 203) {
						// 告诉主线程改UI
						Message msg = new Message();
						msg.what = LOINGOUT;
						handler.sendMessage(msg);
					}
					connection.disconnect();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (ProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		});
		t.start();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
            moveTaskToBack(false);  
            return true;  
        }  
        
		return super.onKeyDown(keyCode, event);
	}

}
