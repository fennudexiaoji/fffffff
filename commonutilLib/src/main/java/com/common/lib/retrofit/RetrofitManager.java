package com.common.lib.retrofit;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.common.lib.BuildConfig;
import com.common.lib.base.BaseApplication;
import com.common.lib.fileutils.FileUtils;
import com.common.lib.global.AppGlobal;
import com.common.lib.retrofit.cookie.CookieManager;
import com.common.lib.retrofit.https.HttpsVerification;
import com.common.lib.utils.LogUtils;
import com.common.lib.utils.NetworkUtil;

import java.io.File;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * retrofit
 */
public class RetrofitManager {
    private final static String TAG = RetrofitManager.class.getSimpleName();
    private static Retrofit retrofit;
    private String mAgent;
    public static final int REQUEST_TIME_OUT = 30;
    public static final TimeUnit REQUEST_TIME_UNIT = TimeUnit.SECONDS;
    private OkHttpClient okHttpClient;
    private RetrofitManager() {
        retrofit=init();
    }

    public Retrofit getRetrofit(){
        return retrofit;
    }

    private static RetrofitManager mRetrofitManager;

    public synchronized static RetrofitManager getInstance() {
        if(mRetrofitManager == null)
            mRetrofitManager = new RetrofitManager();
        return mRetrofitManager;
    }

    public RetrofitManager setAgent(String agent) {
        this.mAgent = agent;
        return this;
    }
    public Retrofit init() {
        OkHttpClient client = getOkHttpClient();
        return new Retrofit.Builder()
                .baseUrl(ApiHttpUrl.HOST)
                .client(client)
                //如果需要既支持String又支持Gson需要先设置ScalarsConverterFactory 后设置 GsonConverterFactory
                // retrofit对于解析器是由添加的顺序分别试用的，解析成功就直接返回，失败则调用下一个解析器。
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        if(okHttpClient==null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.i("请求数据", message);
                }
            });
            CookieManager mCookieJar = new CookieManager(BaseApplication.getInstance());
            //File cacheDir = BaseApplication.getInstance().getCacheDir();
            File cacheDir =FileUtils.getDiskCacheDir(BaseApplication.getInstance());
                    HttpsVerification.SSLParams sslParams = HttpsVerification.getSslSocketFactory();
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .cookieJar(mCookieJar)
                    .addInterceptor(requestInterceptor)
                    //.addInterceptor(cacheInterceptor)
                    .addNetworkInterceptor(loggingInterceptor)
                    .cache(new Cache(cacheDir, 1024 * 1024 * 10))
                    .readTimeout(REQUEST_TIME_OUT, REQUEST_TIME_UNIT)
                    .connectTimeout(REQUEST_TIME_OUT, REQUEST_TIME_UNIT)
                    .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager);
            //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            //BODY打印信息,NONE不打印信息
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
            builder.addInterceptor(loggingInterceptor);
            okHttpClient=builder.build();
        }
        return okHttpClient;
    }

    public <S> S createServer(Class<S> serverClass, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        OkHttpClient client = getOkHttpClient();
        Retrofit retrofit = builder.client(client).build();
        return retrofit.create(serverClass);
    }

    public <S> S createServerInBackgroundThread(Class<S> serverClass, String baseUrl) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .callbackExecutor(backgroundThreadExecutor)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

        OkHttpClient okHttpClient = getOkHttpClient();
        Retrofit retrofit = builder.client(okHttpClient).build();
        return retrofit.create(serverClass);
    }

    private Executor backgroundThreadExecutor = new Executor() {
        private Handler handler;

        @Override
        public void execute(@NonNull final Runnable runnable) {
            if (handler == null) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        handler = new Handler(Looper.myLooper());
                        handler.post(runnable);
                        Looper.loop();
                    }
                });
                thread.start();
            } else {
                handler.post(runnable);
            }
        }
    };

    public RequestBody createRequestBody(@Nullable List<Pair<String, String>> params,
                                                @Nullable List<Pair<String, File>> files) {
        RequestBody formBody = null;
        if (params != null || files != null) {
            if (files == null) {
                FormBody.Builder builder = new FormBody.Builder();
                for (Pair<String, String> param : params) {
                    LogUtils.i(TAG, "params --> " + param.first + "=" + param.second);
                    builder.add(param.first, param.second);
                }
                formBody = builder.build();
            } else {
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                for (Pair<String, File> file : files) {
                    if (file != null) {
                        LogUtils.i(TAG, "file --> " + file.first + "##" + file.second.getPath());
                        builder.addFormDataPart(file.first, file.second.getName(),
                                RequestBody.create(null, file.second));
                    }
                }
                if (params != null) {
                    for (Pair<String, String> param : params) {
                        LogUtils.i(TAG, "params --> " + param.first + "=" + param.second);
                        builder.addFormDataPart(param.first, param.second);
                    }
                }
                formBody = builder.build();
            }
        }
        return formBody;
    }

    //请求拦截--增加基础参数
    private Interceptor requestInterceptor = chain -> {
        Request request = chain.request();
        HttpUrl newUrl = request.url().newBuilder()
                //.addQueryParameter(HttpParam.API_VERSION, Common.API_VERSION)
                .build();
        Request newRequest = request.newBuilder()
                .url(newUrl)
                .addHeader("X-Token", AppGlobal.getToken())//x-auth-token
                .build();
        return chain.proceed(newRequest);
    };

    //请求拦截--离线缓存
    private Interceptor cacheInterceptor = chain -> {
        Request request = chain.request();
        //Logger.i("method="+request.method());
        if (!TextUtils.equals(request.method(), "GET")) {
            return chain.proceed(request);
        }
        boolean cache = !NetworkUtil.isNetworkAvailable(BaseApplication.getInstance());
        if (cache) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
            LogUtils.i("get data from cache");
        }

        Response response = chain.proceed(request);

        if (!cache) {
            int maxAge = 0; // 有网络时 设置缓存超时时间0个小时
            //Logger.i("has network maxAge="+maxAge);
            response = response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    //.removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                    .build();
        } else {
            //Logger.i("response no network");
            int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
            //Logger.i("has maxStale="+maxStale);
            response = response.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    //.removeHeader("Pragma")
                    .build();
            //Logger.i("response build maxStale="+maxStale);
        }
        return response;
    };


}