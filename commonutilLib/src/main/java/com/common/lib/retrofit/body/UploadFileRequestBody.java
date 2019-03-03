package com.common.lib.retrofit.body;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;


public class UploadFileRequestBody extends RequestBody {
    /**
     * 请求体进度回调接口，用于文件上传进度回调
     */
    public interface ProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

    /**
     * 响应体进度回调接口，用于文件下载进度回调
     */
    public interface ProgressResponseListener {
        void onResponseProgress(long bytesRead, long contentLength, boolean done);
    }
    //实际的待包装请求体
    private  RequestBody requestBody;
    //进度回调接口
    private  ProgressRequestListener progressListener;
    //包装完成的BufferedSink
    private BufferedSink bufferedSink;
    private boolean isEnd=false;

    /**
     * 构造函数，赋值
     * @param requestBody 待包装的请求体
     * @param progressListener 回调接口
     */
    public UploadFileRequestBody(RequestBody requestBody, ProgressRequestListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    /**
     * 重写调用实际的响应体的contentType
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (bufferedSink == null) {
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();

    }

    /**
     * 写入，回调进度接口
     * @param sink Sink
     * @return Sink
     */
    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {
            //当前写入字节数
            long writtenBytesCount = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long totalBytesCount = 0L;

            /*@Override
            public void write(Buffer source, long byteCount) throws IOException {
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                //增加当前写入的字节数
                bytesWritten += byteCount;
                //回调
                if(progressListener != null&&!isEnd){
                    if(bytesWritten == contentLength){
                        isEnd=true;
                    }
                    progressListener.onRequestProgress(bytesWritten, contentLength, bytesWritten == contentLength);
                }
                super.write(source, byteCount);
            }*/

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                //增加当前写入的字节数
                writtenBytesCount += byteCount;
                //获得contentLength的值，后续不再调用
                if (totalBytesCount == 0) {
                    totalBytesCount = contentLength();
                }
                Observable.just(writtenBytesCount).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        if(progressListener != null&&!isEnd){
                            if(writtenBytesCount == totalBytesCount){
                                isEnd=true;
                            }
                            progressListener.onRequestProgress(writtenBytesCount, totalBytesCount, isEnd);
                        }
                    }
                });
            }

        };

    }
}
