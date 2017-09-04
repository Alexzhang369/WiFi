package zttokhttp.response;

/**
 * raw 字符串结果回调
 * Created by tsy on 16/8/18.
 */
public abstract class ZTTRawResponseHandler implements ZTTIResponseHandler {

    public abstract void onSuccess(int statusCode, String response);

    @Override
    public void onProgress(long currentBytes, long totalBytes) {

    }
}
