package com.example.employeelist.network;



import com.example.employeelist.callback.EmployeeListCallback;
import com.example.employeelist.model.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public final class ApiClient {

    private static Retrofit retrofit = null;

    private static ApiService build() {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ApiConstants.HOST)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }

    public static void enqueue(final EmployeeListCallback employeeListCallback) {
        Call<ResponseBody> call = build().getEmployeeList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                try {
                    if (response.code() == 200) {
                        employeeListCallback.onSuccess(JSONDecode.convertJSONtoList(response.body().string()));
                    } else {
                        employeeListCallback.onFailure(Constants.ERROR_SUCCESS);
                    }
                } catch (Exception e) {
                    employeeListCallback.onFailure(Constants.ERROR_UNKNOWN);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                employeeListCallback.onFailure(Constants.ERROR_SUCCESS);
            }
        });
    }
}
