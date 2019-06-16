package com.neo.chatkitephemeralmessage


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private var retrofit: Retrofit? = null
    val client: Retrofit
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(ChatkitApp.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(OkHttpClient())
                    .build()
            }
            return retrofit!!
        }
}