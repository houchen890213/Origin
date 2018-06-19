package com.yulin.io.retrofit.okhttp;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.yulin.io.retrofit.config.NetworkConfig;
import com.yulin.io.retrofit.interceptor.LogInterceptor;
import com.yulin.io.retrofit.interceptor.SignInterceptor;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yaowang on 2017/5/3.
 */

public class OkHttpUtils {
    private static OkHttpUtils okHttpUtils = new OkHttpUtils();

    public static OkHttpUtils getInstance() {
        return okHttpUtils;
    }

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private NetworkConfig config;

    public void initConfig(NetworkConfig config) {
        this.config = config;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.connectTimeout(config.getConnectTimeout(), TimeUnit.SECONDS);
        builder.writeTimeout(config.getWriteTimeout(), TimeUnit.SECONDS);
        builder.readTimeout(config.getReadTimeout(), TimeUnit.SECONDS);
        SignInterceptor signInterceptor = new SignInterceptor(config.getSignCalculate(), config.getCommParameter());
        builder.addInterceptor(signInterceptor);
        builder.addInterceptor(new LogInterceptor());
        okHttpClient = builder.build();
        createRetrofit();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public NetworkConfig getConfig() {
        return config;
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(config.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public void setCertificates(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            okHttpClient = okHttpClient.newBuilder().sslSocketFactory(sslContext.getSocketFactory()).build();
            createRetrofit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

}
