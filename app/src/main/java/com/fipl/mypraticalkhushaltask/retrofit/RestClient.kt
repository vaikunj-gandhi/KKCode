package com.fipl.mypraticalkhushaltask.retrofit


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RestClient {

    companion object {
        val apiType = "Android"
        val data = "form-multi-part"

        val user_type = "2"

        var token = "fs8iKiwm1EM:APA91bHktMKWaDIuUGZAyrZyzVoAcFngmVnOlbFFvTgzfUOayty1Pxyd0lRTCrW-Shap20D6djedphekA3gTn1wsOXbzbnTHjpPb-BtZ37sSzJB395ElPWdVqbvaTwIOfvQPJ3Glh02m"

         // Live server url use this base URL
//

        // Live server url
//        http://blue.alphademo.in/development/interview/services/
        var BASEURL = "http://blue.alphademo.in/"

        var base_url = BASEURL+"development/interview/services/"
        var image_base_url = BASEURL+"backend/web/uploads/"
        var imageBaseUrl = BASEURL+"backend/web/uploads/"

        var googleBaseUrl = "https://maps.googleapis.com/maps/api/"
        var base_url_phase1 = base_url

        internal var REST_CLIENT: RestApi? = null

        private var restAdapter: Retrofit? = null
        internal var REST_GOOGLE_CLIENT: RestApi? = null
        private var restGoogleAdapter: Retrofit? = null

        init {
            setupRestClient()
            setupRestGoogleClient()
        }

        fun setupRestGoogleClient() {

            val gson = GsonBuilder().setLenient().create()
            restGoogleAdapter = Retrofit.Builder()
                .baseUrl(googleBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(
                    setOkHttpClientBuilder()
                        .build()
                )
                .build()
        }

        fun setOkHttpClientBuilder(): OkHttpClient.Builder {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(loggingInterceptor)
            builder.connectTimeout(300, TimeUnit.SECONDS)
            builder.readTimeout(80, TimeUnit.SECONDS)
            builder.writeTimeout(90, TimeUnit.SECONDS)
            return builder
        }


        fun setupRestClient() {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val builder = OkHttpClient.Builder()
            builder.addInterceptor(loggingInterceptor)
            builder.connectTimeout(300, TimeUnit.SECONDS)
            builder.readTimeout(100, TimeUnit.SECONDS)
            builder.writeTimeout(100, TimeUnit.SECONDS)
            val gson = GsonBuilder().setLenient().create()
            restAdapter = Retrofit.Builder()
                .baseUrl(base_url_phase1)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(
                    builder
                        .build()
                )
                .build()
        }


        fun get(): RestApi? {
            if (REST_CLIENT == null) {
                REST_CLIENT = restAdapter!!.create(RestApi::class.java)
            }
            return REST_CLIENT
        }

        fun getGoogle(): RestApi? {
            if (REST_GOOGLE_CLIENT == null) {
                REST_GOOGLE_CLIENT = restGoogleAdapter!!.create(
                    RestApi::class.java
                )

            }
            return REST_GOOGLE_CLIENT
        }

    }
}


