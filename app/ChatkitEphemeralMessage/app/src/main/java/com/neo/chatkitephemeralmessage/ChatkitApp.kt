package com.neo.chatkitephemeralmessage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import com.pusher.chatkit.CurrentUser
import com.pusher.chatkit.messages.multipart.NewPart
import com.pusher.util.Result
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class ChatkitApp {
    companion object {
        lateinit var currentUser: CurrentUser
        const val BASE_URL = "https://06063814.ngrok.io/"
        const val INSTANCE_LOCATOR = "v1:us1:cc3aba0a-bc81-457d-924b-d1a1ba11dfdd"
        val TAG = ChatkitApp::class.java.simpleName
    }

    fun sendMessage(text: String, imageUrl: String?) {
        if (!imageUrl.isNullOrEmpty()) {
            ChatkitApp.currentUser.sendMultipartMessage(
                roomId = ChatkitApp.currentUser.rooms[0].id,
                parts = listOf(
                    NewPart.Inline(text, "text/plain"),
                    NewPart.Attachment(
                        type = "image/jpeg",
                        file = File(imageUrl).inputStream(),
                        name = "myImage.jpg",
                        customData = mapOf("source" to "camera")
                    )
                ),
                callback = { result ->
                }
            )
        }
    }

    fun deleteMessage(messageId: Int) {
        val api = ApiClient.client.create(ApiInterface::class.java)
        api?.deleteMessage(DeleteMessage(messageId, 10000))?.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            }
        })
    }

    fun getBitmapFromUrl(imageUrl: String?): Bitmap? {
        if (imageUrl == null) {
            return null
        }
        return try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            null
        }
    }

}