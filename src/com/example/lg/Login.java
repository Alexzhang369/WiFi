package com.example.lg;


import java.io.*;
import java.net.*;

public class Login
{

 public Login()
 {
 }

 public static void main(String args[])
 {
     Login();
 }

 public static void Login()
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
         String content = (new StringBuilder("DDDDD=")).append(URLEncoder.encode("14120365", "UTF-8")).toString();
         content = (new StringBuilder(String.valueOf(content))).append("&upass=").append(URLEncoder.encode("2008lonely", "UTF-8")).toString();
         content = (new StringBuilder(String.valueOf(content))).append("&0MKKey=%B5%C7%C2%BC%28Login%29").toString();
         content = (new StringBuilder(String.valueOf(content))).append("&C2=on").toString();
         out.writeBytes(content);
         out.flush();
         out.close();
         connection.getInputStream();
         connection.disconnect();
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
