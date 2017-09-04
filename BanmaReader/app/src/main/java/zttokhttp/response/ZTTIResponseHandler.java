package zttokhttp.response;

/**
 * Created by tsy on 16/8/15.
 */
public interface ZTTIResponseHandler {

    void onFailure(int statusCode, String error_msg);

    void onProgress(long currentBytes, long totalBytes);
}
