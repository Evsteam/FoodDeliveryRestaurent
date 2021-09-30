package com.evs.foodexp.commonPkg.retrofit;

import com.evs.foodexp.BuildConfig;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StripeApiService {


    private static Retrofit retrofit;
    private static final String BASE_URL = "https://www.expressplusnow.com/appbackend/webroot/strip_master/cardprocess_test.php/";
//    private static final String BASE_URL = "https://www.expressplusnow.com/appbackend/webroot/strip_master/cardprocess.php/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .readTimeout(90, TimeUnit.SECONDS)
            .writeTimeout(90, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS);

    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

    private static OkHttpClient.Builder getHttpClient() {
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            httpClient.addInterceptor(new LogInterceptor());
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
            httpClient.addInterceptor(interceptor);
        }
        return httpClient;
    }


    public static APIServiceInterface getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient().build())
                    .build();
        }
        return retrofit.create(APIServiceInterface.class);
    }

}
