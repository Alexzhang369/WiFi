//package com.codingbingoZTT.fastreader.TTS;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.google.tts.TextToSpeechBeta;
//import com.google.tts.TextToSpeechBeta.OnInitListener;
//import com.ztt.sql.R;
//
//import java.util.Locale;
//
//public class TTSUtils extends Activity implements OnInitListener {
//    /** Called when the activity is first created. */
//    private Button mBtn;
//    private EditText mText;
//    //使用com.google.tts包中的TextToSpeechBeta
//    private TextToSpeechBeta mTTS;
//
//    private static final String TAG = "ZTT ";
//    private static final int REQ_TTS_STATUS_CHECK = 0;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);
//
//        //检查TTS数据是否已经安装并且可用
//        Intent checkIntent = new Intent();
//        checkIntent.setAction(TextToSpeechBeta.Engine.ACTION_CHECK_TTS_DATA);
//        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);
//
//        mText = (EditText)findViewById(R.id.ttstext);
//        mBtn = (Button) findViewById(R.id.ttsbtn);
//        mBtn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                String ttsText = mText.getText().toString();
//                if(ttsText != "")
//                {
//                    //读取文本框中的中文
//                    mTTS.speak(ttsText, TextToSpeechBeta.QUEUE_ADD, null);
//                }
//            }
//        });
//    }
//    //实现TTS初始化接口
//    @Override
//    public void onInit(int status, int version) {
//        // TODO Auto-generated method stub
//        Log.v(TAG, "version = " + String.valueOf(version));
//        //判断TTS初始化的返回版本号，如果为-1，表示没有安装对应的TTS数据
//        if(version == -1)
//        {
//            //提示安装所需的TTS数据
//            //alertInstallEyesFreeTTSData();
//            //TTS Engine初始化完成
//            if(status == TextToSpeechBeta.SUCCESS)
//            {
//                Log.v(TAG, "success to init tts");
//                //设置TTS引擎，com.google.tts即eSpeak支持的语言包含中文，使用Android系统默认的pico可以设置为com.svox.pico
//                mTTS.setEngineByPackageName("com.google.tts");
//                int result = mTTS.setLanguage(Locale.CHINA);
//                //设置发音语言
//                if(result == TextToSpeechBeta.LANG_MISSING_DATA || result == TextToSpeechBeta.LANG_NOT_SUPPORTED)
//                //判断语言是否可用
//                {
//                    Log.v(TAG, "Language is not available");
//                    mBtn.setEnabled(false);
//                }
//                else
//                {
//                    mTTS.speak("你好,斑马听书!", TextToSpeechBeta.QUEUE_ADD, null);
//                    mBtn.setEnabled(true);
//                }
//            }
//            else
//            {
//                Log.v(TAG, "failed to init tts");
//            }
//        }
//        else
//        {
//            //TTS Engine初始化完成
//            if(status == TextToSpeechBeta.SUCCESS)
//            {
//                Log.v(TAG, "success to init tts");
//                //设置TTS引擎，com.google.tts即eSpeak支持的语言包含中文，使用Android系统默认的pico可以设置为com.svox.pico
//                mTTS.setEngineByPackageName("com.google.tts");
//                int result = mTTS.setLanguage(Locale.CHINA);
//                //设置发音语言
//                if(result == TextToSpeechBeta.LANG_MISSING_DATA || result == TextToSpeechBeta.LANG_NOT_SUPPORTED)
//                //判断语言是否可用
//                {
//                    Log.v(TAG, "Language is not available");
//                    mBtn.setEnabled(false);
//                }
//                else
//                {
//                    mTTS.speak("你好,朋友!", TextToSpeechBeta.QUEUE_ADD, null);
//                    mBtn.setEnabled(true);
//                }
//            }
//            else
//            {
//                Log.v(TAG, "failed to init tts");
//            }
//        }
//    }
//
//    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == REQ_TTS_STATUS_CHECK)
//        {
//            switch (resultCode) {
//                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_PASS:
//                    //这个返回结果表明TTS Engine可以用
//                {
//                    //使用的是TextToSpeechBeta
//                    mTTS = new TextToSpeechBeta(this, this);
//                    Log.v(TAG, "TTS Engine is installed!");
//
//                }
//
//                break;
//                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_BAD_DATA:
//                    //需要的语音数据已损坏
//                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_MISSING_DATA:
//                    //缺少需要语言的语音数据
//                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
//                    //缺少需要语言的发音数据
//                {
//                    //这三种情况都表明数据有错,重新下载安装需要的数据
//                    Log.v(TAG, "Need language stuff:" + resultCode);
//                    Intent dataIntent = new Intent();
//                    dataIntent.setAction(TextToSpeechBeta.Engine.ACTION_INSTALL_TTS_DATA);
//                    startActivity(dataIntent);
//                }
//                break;
//                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_FAIL:
//                    //检查失败
//                default:
//                    Log.v(TAG, "Got a failure. TTS apparently not available");
//                    break;
//            }
//        }
//        else
//        {
//            //其他Intent返回的结果
//        }
//    }
//    //弹出对话框提示安装所需的TTS数据
////    private void alertInstallEyesFreeTTSData()
////    {
////        Builder alertInstall = new AlertDialog.Builder(this)
////                .setTitle("缺少需要的语音包")
////                .setMessage("下载安装缺少的语音包")
////                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        // TODO Auto-generated method stub
////                        //下载eyes-free的语音数据包
////                        String ttsDataUrl = "http://eyes-free.googlecode.com/files/tts_3.1_market.apk";
////                        Uri ttsDataUri = Uri.parse(ttsDataUrl);
////                        Intent ttsIntent = new Intent(Intent.ACTION_VIEW, ttsDataUri);
////                        startActivity(ttsIntent);
////                    }
////                })
////                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
////
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////                        // TODO Auto-generated method stub
////
////                    }
////                });
////        alertInstall.create().show();
////
////    }
////
////    @Override
////    protected void onDestroy() {
////        // TODO Auto-generated method stub
////        super.onDestroy();
////        if(mTTS!=null){
////            mTTS.shutdown();
////        }
////    }
//
//    @Override
//    protected void onPause() {
//        // TODO Auto-generated method stub
//        super.onPause();
//        if(mTTS != null)
//        {
//            mTTS.stop();
//        }
//    }
//
//}