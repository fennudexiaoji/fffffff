package com.common.lib.retrofit.body;

import com.common.lib.retrofit.rxandroid.UploadOnSubscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;


public class UploadRequestBody extends RequestBody {
    private File mFile;
    private static final int DEFAULT_BUFFER_SIZE = 2048;
    public UploadRequestBody(File mFile) {
        this.mFile = mFile;
    }
    private UploadOnSubscribe mUploadOnSubscribe;

    public void setUploadOnSubscribe(UploadOnSubscribe uploadOnSubscribe) {
        this.mUploadOnSubscribe = uploadOnSubscribe;
    }
    //application/octet-stream 只能提交二进制，而且只能提交一个二进制，如果提交文件的话，只能提交一个文件,后台接收参数只能有一个，而且只能是流（或者字节数组）
    //multipart/form-data:既可以提交普通键值对，也可以提交(多个)文件键值对。
    @Override
    public MediaType contentType() {
        return MediaType.parse("multipart/form-data");
    }
    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                // update progress on UI thread
                if(mUploadOnSubscribe != null) {
                    mUploadOnSubscribe.onRead(read);
                }
                sink.write(buffer, 0, read);
            }

        } finally {
            in.close();
        }

    }


}
