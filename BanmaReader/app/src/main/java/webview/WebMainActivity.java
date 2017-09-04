package webview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.codingbingoZTT.BanmaReader.R;

import java.io.File;

import zttokhttp.ZTTOkHttp;
import zttokhttp.response.ZTTDownloadResponseHandler;
import zttokhttp.util.ToastUtils;
import zttokhttp.util.ZTTLogUtils;


public class WebMainActivity extends AppCompatActivity {
    WebView mWebview;
    WebSettings mWebSettings;
    TextView beginLoading,endLoading,loading,mtitle;
    private static final String TAG = "WebMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_web);



        mWebview = (WebView) findViewById(R.id.webView1);
        beginLoading = (TextView) findViewById(R.id.text_beginLoading);
        endLoading = (TextView) findViewById(R.id.text_endLoading);
        loading = (TextView) findViewById(R.id.text_Loading);
        mtitle = (TextView) findViewById(R.id.title);

        mWebSettings = mWebview.getSettings();


        mWebview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //在这里进行下载的处理。
                // 如果你没有进行处理，一般APP就不会开始下载行为，在这里可以自己开启一个线程来下载

                //getExternalStorageDirectory()
                ZTTOkHttp.get(getApplicationContext()).download(url,
                         "/data/data/com.codingbingoZTT.fastreader"+ "/TXT_Download", System.currentTimeMillis() + ".txt",
                        new ZTTDownloadResponseHandler() {
                            @Override
                            public void onFinish(File download_file) {
                                ZTTLogUtils.e(TAG, "onFinish:" + download_file.getPath());
                                ToastUtils.showToastSafe(getApplicationContext(), "TXT小说下载完成");
                            }

                            @Override
                            public void onProgress(long currentBytes, long totalBytes) {
                                ZTTLogUtils.e(TAG, currentBytes + "/" + totalBytes);
                                ToastUtils.showToastSafe(getApplicationContext(), "TXT小说下载中" + currentBytes + "/" + totalBytes);
                            }

                            @Override
                            public void onFailure(String error_msg) {
                                ZTTLogUtils.e(TAG, error_msg);
                                ToastUtils.showToastSafe(getApplicationContext(), "TXT小说下载完成失败" + error_msg);
                            }
                        });


                ZTTLogUtils.e("download", "url" + url);
                ZTTLogUtils.e("download", "contentDisposition" + contentDisposition);
                ZTTLogUtils.e("download", "mimetype" + mimetype);
            }
        });
        //mWebview.loadUrl("https://www.hao123.com/");
        mWebview.loadUrl("http://www.txt53.com");


        //设置不用系统浏览器打开,直接显示在当前Webview
        mWebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        //设置WebChromeClient类
        mWebview.setWebChromeClient(new WebChromeClient() {


            //获取网站标题
            @Override
            public void onReceivedTitle(WebView view, String title) {
                System.out.println("标题在这里");
                mtitle.setText(title);
            }


            //获取加载进度
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress < 100) {
                    String progress = newProgress + "%";
                    loading.setText(progress);
                } else if (newProgress == 100) {
                    String progress = newProgress + "%";
                    loading.setText(progress);
                }
            }
        });


        //设置WebViewClient类
        mWebview.setWebViewClient(new WebViewClient() {
            //设置加载前的函数
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                System.out.println("开始加载了");
                beginLoading.setText("开始加载了");

            }

            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                endLoading.setText("结束加载了");

            }
        });
    }

    //点击返回上一页面而不是退出浏览器
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebview.canGoBack()) {
            mWebview.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    //销毁Webview
    @Override
    protected void onDestroy() {
        if (mWebview != null) {
            mWebview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebview.clearHistory();

            ((ViewGroup) mWebview.getParent()).removeView(mWebview);
            mWebview.destroy();
            mWebview = null;
        }
        super.onDestroy();
    }
}
