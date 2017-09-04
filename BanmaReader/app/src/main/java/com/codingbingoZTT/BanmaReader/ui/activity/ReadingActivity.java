package com.codingbingoZTT.BanmaReader.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.codingbingoZTT.BanmaReader.Constants;
import com.codingbingoZTT.BanmaReader.R;
import com.codingbingoZTT.BanmaReader.base.BaseActivity;
import com.codingbingoZTT.BanmaReader.dao.Book;
import com.codingbingoZTT.BanmaReader.dao.BookDao;
import com.codingbingoZTT.BanmaReader.ui.fragment.ChapterListFragment;
import com.codingbingoZTT.BanmaReader.ui.fragment.ReadingFragment;
import com.codingbingoZTT.BanmaReader.view.readview.PageFactory;
import com.google.tts.TextToSpeechBeta;

import java.util.List;
import java.util.Locale;


/**
 *
 *
 * By 2017/1/11.
 */

public class ReadingActivity extends BaseActivity implements View.OnClickListener,TextToSpeechBeta.OnInitListener {
    private ChapterListFragment mChapterListFragment;
    private ReadingFragment mReadingFragment;

    private long bookId = NO_BOOK_ID;
    private String bookPath;
    private  static  int  voiceSwitch=0;

    //使用com.google.tts包中的TextToSpeechBeta
    private TextToSpeechBeta mTTS;

    private static final String TAG = "ZTT ";
    private static final int REQ_TTS_STATUS_CHECK = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //进入activity，先进入全屏状态
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(params);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        Intent checkIntent = new Intent();
        checkIntent.setAction(TextToSpeechBeta.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);

        setContentView(R.layout.activity_reading);


        init();
        initView();

    }

    //实现TTS初始化接口
    @Override
    public void onInit(int status, int version) {
        // TODO Auto-generated method stub
        Log.v(TAG, "version = " + String.valueOf(version));
        //判断TTS初始化的返回版本号，如果为-1，表示没有安装对应的TTS数据
        if(version == -1)
        {
            //提示安装所需的TTS数据
            //alertInstallEyesFreeTTSData();
            //TTS Engine初始化完成
            if(status == TextToSpeechBeta.SUCCESS)
            {
                Log.v(TAG, "success to init tts");
                //设置TTS引擎，com.google.tts即eSpeak支持的语言包含中文，使用Android系统默认的pico可以设置为com.svox.pico
                mTTS.setEngineByPackageName("com.google.tts");
                int result = mTTS.setLanguage(Locale.CHINA);
                //设置发音语言
                if(result == TextToSpeechBeta.LANG_MISSING_DATA || result == TextToSpeechBeta.LANG_NOT_SUPPORTED)
                //判断语言是否可用
                {
                    Log.v(TAG, "Language is not available");
                    //mBtn.setEnabled(false);
                }
                else
                {
                    mTTS.speak("你好,斑马听书!", TextToSpeechBeta.QUEUE_ADD, null);

                }
            }
            else
            {
                Log.v(TAG, "failed to init tts");
            }
        }
        else
        {
            //TTS Engine初始化完成
            if(status == TextToSpeechBeta.SUCCESS)
            {
                Log.v(TAG, "success to init tts");
                //设置TTS引擎，com.google.tts即eSpeak支持的语言包含中文，使用Android系统默认的pico可以设置为com.svox.pico
                mTTS.setEngineByPackageName("com.google.tts");
                int result = mTTS.setLanguage(Locale.CHINA);
                //设置发音语言
                if(result == TextToSpeechBeta.LANG_MISSING_DATA || result == TextToSpeechBeta.LANG_NOT_SUPPORTED)
                //判断语言是否可用
                {
                    Log.v(TAG, "Language is not available");
                }
                else
                {
                    mTTS.speak("你好,朋友!", TextToSpeechBeta.QUEUE_ADD, null);
                }
            }
            else
            {
                Log.v(TAG, "failed to init tts");
            }
        }
    }


    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_TTS_STATUS_CHECK)
        {
            switch (resultCode) {
                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_PASS:
                    //这个返回结果表明TTS Engine可以用
                {
                    //使用的是TextToSpeechBeta
                    mTTS = new TextToSpeechBeta(this, this);
                    Log.v(TAG, "TTS Engine is installed!");

                }

                break;
                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_BAD_DATA:
                    //需要的语音数据已损坏
                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_MISSING_DATA:
                    //缺少需要语言的语音数据
                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_MISSING_VOLUME:
                    //缺少需要语言的发音数据
                {
                    //这三种情况都表明数据有错,重新下载安装需要的数据
                    Log.v(TAG, "Need language stuff:"+resultCode);
                    Intent dataIntent = new Intent();
                    dataIntent.setAction(TextToSpeechBeta.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(dataIntent);
                }
                break;
                case TextToSpeechBeta.Engine.CHECK_VOICE_DATA_FAIL:
                    //检查失败
                default:
                    Log.v(TAG, "Got a failure. TTS apparently not available");
                    break;
            }
        }
        else
        {
            //其他Intent返回的结果
        }
    }
    private void init() {
        Intent intent = getIntent();
        if (intent.hasExtra("type") == false) {
            finish();
            return;
        }

        switch (intent.getIntExtra("type", Constants.TYPE_FROM_MAIN_ACTIVITY)) {
            case Constants.TYPE_FROM_MAIN_ACTIVITY:
                //从主页面进来的，说明本地数据已经插入数据库了
                bookId = intent.getLongExtra("bookId", 0);
                break;
            case Constants.TYPE_FROM_LOCAL_FILE_ACTIVITY:
                bookPath = intent.getStringExtra("bookPath");
                //防止用户再从文件列表页点击进入
                List<Book> bookList = getDaoSession().getBookDao().queryBuilder().where(BookDao.Properties.BookPath.eq(bookPath)).list();
                if (bookList.size() != 0) {
                    bookId = bookList.get(0).getId();
                    break;
                }
        }
    }

    private void initView() {
        mReadingFragment = new ReadingFragment();
        mReadingFragment.setBookId(bookId);
        mReadingFragment.setBookPath(bookPath);
        mReadingFragment.setOnClickListener(this);

        mChapterListFragment = new ChapterListFragment();
        mChapterListFragment.setBookId(bookId);
        mChapterListFragment.setBookPath(bookPath);

        if (mReadingFragment.isAdded()){
            getFragmentManager()
                    .beginTransaction()
                    .show(mReadingFragment)
                    .commitAllowingStateLoss();
        } else{
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.reading_container, mReadingFragment)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.backBtn:
                finish();
                break;
            case R.id.readBtn:
                //String ttsText =mReadingFragment.getmBook().getBookName() ;

                if(voiceSwitch %2 ==0) {

                    List<String> mLines = PageFactory.getIstance(this).getmTTSLines();
                    for (String TTSline : mLines) {
                        Log.e(TAG, "TTSline---" + TTSline);
                        if (TTSline.endsWith("@")) { //去除段位的@
                            TTSline = TTSline.substring(0, TTSline.length() - 1);
                        }
                        if (TTSline != "" && TTSline != "@") {
                            //读取中文
                            mTTS.speak(TTSline, TextToSpeechBeta.QUEUE_ADD, null);
                        }

                    }
                    voiceSwitch++;
                }else {
                    mTTS.stop();
                    voiceSwitch++;
                }
                //mReadingFragment.nextPage();
                break;

            case R.id.book_contents:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.reading_container, mChapterListFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mReadingFragment.isHidden() == false) {
            if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                mReadingFragment.nextPage();
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                mReadingFragment.prePage();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(mTTS!=null){
            mTTS.shutdown();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        if(mTTS != null)
        {
            mTTS.stop();
        }
    }


}
