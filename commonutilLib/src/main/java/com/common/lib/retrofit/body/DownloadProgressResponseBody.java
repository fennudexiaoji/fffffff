package com.common.lib.retrofit.body;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by 7du-28 on 2018/7/12.
 */

public class DownloadProgressResponseBody extends ResponseBody{
    private ResponseBody responseBody;
    //总字节长度，避免多次调用contentLength()方法
    long contentLength = 0L;
    private DownloadCallbackListener downloadListener;
    public interface DownloadCallbackListener{

        public void onProgress(int percent, long fileSizeDownloaded, long fileSize);

    }

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public DownloadProgressResponseBody(ResponseBody responseBody, DownloadCallbackListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
        this.contentLength=responseBody.contentLength();
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            //当前写入字节数
            long currentBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Long a = new Long(totalBytesRead);
                Long b = new Long(currentBytesRead);
                if (null != downloadListener&&!a.equals(b)) {
                    if (bytesRead != -1) {
                        downloadListener.onProgress((int) (totalBytesRead * 100 / contentLength),totalBytesRead,contentLength);
                        currentBytesRead=totalBytesRead;
                    }

                }
                return bytesRead;
            }
        };

    }
}
