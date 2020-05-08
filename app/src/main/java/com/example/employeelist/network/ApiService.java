package com.example.employeelist.network;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

interface ApiService {

    @Headers("Content-Type: application/json")
    @GET(ApiConstants.EMPLOYEE)
    Call<ResponseBody> getEmployeeList();
}
