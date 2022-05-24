package team.space.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

import static team.space.utils.Shared.LOGGED_USER;

public class HandleRequests {


    /**
     * This is to make a single access object on the client to enhance performance
     */
    public static OkHttpClient getHttpClientCache() {


        return new OkHttpClient
                .Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(HeaderInterceptor())
                .build();
    }
    /**
     * This is to make a single access object on the client to enhance performance
     */
    public static OkHttpClient getHttpClient() {


        return new OkHttpClient
                .Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)

                .build();
    }


    /**
     * Add required headers if they have been saved as of yet.
     */
    private static Interceptor HeaderInterceptor() {
        return chain -> {
            okhttp3.Request request = chain.request();

            request = request.newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + LOGGED_USER.getAccessToken().trim())
                  //  .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrYWppdmFraW5zbGV5QGdtYWlsLmNvbSIsImNvbXBhbnlJZCI6IjhmNDIzMDJkLWMwZWUtNGY2Yy04OGQ2LWQwNjg1YWFhYTg4YSIsInJvbGUiOjEsImlzcyI6Ii9hdXRoL2FwaS92MS91c2Vycy9sb2dpbiIsImZ1bGxOYW1lIjoiS3VtYmlyYWkiLCJleHAiOjE2NTE2MzAzMDYsInVzZXJJZCI6MH0.HjdExR1MyfFn_OGSb7xX33-iEEkh-eUA0zpsgE_xy3Q" )
                    .build();
           // System.out.println("Request: " + LOGGED_USER.getAccessToken().toString());

            return chain.proceed(request);
        };
    }

}
