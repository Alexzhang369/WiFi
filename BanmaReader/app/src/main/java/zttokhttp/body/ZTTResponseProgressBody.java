package zttokhttp.body;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import zttokhttp.response.ZTTDownloadResponseHandler;

/**
 * 重写responsebody 设置下载进度监听
 * Created by tsy on 16/8/16.
 */
public class ZTTResponseProgressBody extends ResponseBody {

    private ResponseBody mResponseBody;
    private ZTTDownloadResponseHandler mZTTDownloadResponseHandler;
    private BufferedSource bufferedSource;

    public ZTTResponseProgressBody(ResponseBody responseBody, ZTTDownloadResponseHandler ZTTDownloadResponseHandler) {
        this.mResponseBody = responseBody;
        this.mZTTDownloadResponseHandler = ZTTDownloadResponseHandler;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(mResponseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {

            long totalBytesRead;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += ((bytesRead != -1) ? bytesRead : 0);
                if (mZTTDownloadResponseHandler != null) {
                    mZTTDownloadResponseHandler.onProgress(totalBytesRead, mResponseBody.contentLength());
                }
                return bytesRead;
            }
        };
    }
}
