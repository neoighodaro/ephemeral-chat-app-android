package com.neo.chatkitephemeralmessage


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST("delete-message")
    fun deleteMessage(@Body body: DeleteMessage): Call<ResponseBody>

}