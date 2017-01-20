package me.mrlliu.retrofit_https;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by LiuKang on 2017/1/20.
 */

public interface IMyService {
    @GET("welcome-to-ghost/")
    Call<ResponseBody> getData();
}
