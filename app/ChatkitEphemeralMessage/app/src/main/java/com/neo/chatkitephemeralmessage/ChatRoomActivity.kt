package com.neo.chatkitephemeralmessage


import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.neo.chatkitephemeralmessage.ChatkitApp.Companion.currentUser
import com.pusher.chatkit.rooms.RoomListeners
import kotlinx.android.synthetic.main.activity_chat_room.*
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import java.io.IOException

class ChatRoomActivity : AppCompatActivity() {

    private val chatRoomAdapter = ChatRoomAdapter()
    private var imagePath : String? = null
    private val GALLERY_REQUEST_CODE = 10001
    val chatkitApp = ChatkitApp()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setupRecyclerView()
        subscribeToRoom()
        setupButtonListener()
    }


    private fun setupRecyclerView() {
        with(recyclerViewMessages){
            layoutManager = LinearLayoutManager(this@ChatRoomActivity)
            adapter = chatRoomAdapter
            addItemDecoration(DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL))
        }
    }


    private fun subscribeToRoom() {
        currentUser.subscribeToRoomMultipart(
            roomId = currentUser.rooms[0].id,
            listeners = RoomListeners(
                onMultipartMessage = {
                    runOnUiThread {
                        chatRoomAdapter.addMessage(it)
                    }
                }
            ),
            messageLimit = 20, // Optional
            callback = { subscription ->

            }
        )
    }


    private fun setupButtonListener() {
        addImageButton.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
    }


    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // Result code is RESULT_OK only if the user selects an Image
        if (resultCode == Activity.RESULT_OK)
            when (requestCode) {
                GALLERY_REQUEST_CODE -> {

                    val filePath = data?.data
                    try {
                        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                        val cursor = contentResolver.query(filePath, filePathColumn, null, null, null)
                        cursor!!.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        val imgDecodableString = cursor.getString(columnIndex)
                        cursor.close()
                        imagePath = imgDecodableString
                        Log.e("TAG", imgDecodableString)
                        chatkitApp.sendMessage("", imagePath)
                        imagePath = ""
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
    }


}
