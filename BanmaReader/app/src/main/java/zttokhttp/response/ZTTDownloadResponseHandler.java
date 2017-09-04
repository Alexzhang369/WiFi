package zttokhttp.response;

import java.io.File;

/**
 * 下载回调
 * Created by tsy on 16/8/16.
 */
public abstract class ZTTDownloadResponseHandler {

    public abstract void onFinish(File download_file);
    public abstract void onProgress(long currentBytes, long totalBytes);
    public abstract void onFailure(String error_msg);
}
