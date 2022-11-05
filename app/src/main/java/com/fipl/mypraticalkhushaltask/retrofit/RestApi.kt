package com.fipl.mypraticalkhushaltask.retrofit

import com.fipl.mypraticalkhushaltask.pojo.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


public interface RestApi {

    @FormUrlEncoded
    @POST("users/userAuthentication.php")
    fun login(@Field("action") json: String,@Field("data") jsondata: String,@Field("email") jsonemail: String,
              @Field("password") jsonpassword: String,@Field("user_type") jsonuser_type: String,@Field("devicetoken") jsondevicetoken: String,
              @Field("devicetype") jsondevicetype: String): retrofit2.Call<RegisterPojo>


    @Multipart
    @POST("users/userRegistration.php")
    fun registration(@Part("action") json: RequestBody, @Part("data") jsondata: RequestBody,
                     @Part("email") jsonemail: RequestBody, @Part("password") jsonpassword: RequestBody,
                     @Part("firstname") jsonfirstname: RequestBody, @Part("lastname") jsonlastname: RequestBody,
                     @Part("age") jsonage: RequestBody, @Part("gender") jsongender: RequestBody,
                     @Part("user_type") jsonuser_type: RequestBody, @Part("devicetoken") jsondevicetoken: RequestBody,
                     @Part("devicetype") jsondevicetype:RequestBody, @Part filePart: MultipartBody.Part): retrofit2.Call<RegisterPojo>

    @FormUrlEncoded
    @POST("users/getUserDetails.php")
    fun getUserDetails(@Field("action") json: String,@Field("data") jsondata: String,@Field("user_id") jsonuser_id: String,): retrofit2.Call<RegisterPojo>


}
// https://stackoverflow.com/questions/40607862/retrofit-throwing-an-exception-java-lang-illegalargumentexception-only-one-en
