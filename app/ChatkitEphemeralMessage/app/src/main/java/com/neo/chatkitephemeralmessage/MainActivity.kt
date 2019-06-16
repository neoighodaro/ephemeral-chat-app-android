package com.neo.chatkitephemeralmessage

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pusher.chatkit.AndroidChatkitDependencies
import com.pusher.chatkit.ChatManager
import com.pusher.chatkit.ChatkitTokenProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginButton.setOnClickListener { setupChatManager() }
        checkPermission()
    }

    private fun setupChatManager() {
        val chatManager = ChatManager(
            instanceLocator = ChatkitApp.INSTANCE_LOCATOR,
            userId = username.text.toString(),
            dependencies = AndroidChatkitDependencies(
                tokenProvider = ChatkitTokenProvider(
                    endpoint = "${ChatkitApp.BASE_URL}token",
                    userId = username.text.toString()
                ),
                context = this.applicationContext
            )
        )

        chatManager.connect { result ->
            when (result) {
                is com.pusher.util.Result.Success -> {
                    ChatkitApp.currentUser = result.value
                    startActivity(Intent(this@MainActivity,ChatRoomActivity::class.java))
                    finish()
                }

                is com.pusher.util.Result.Failure -> {
                }
            }
        }

    }

    private fun checkPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_READ_EXTERNAL_STORAGE)

            }
        } else {
            // Permission has already been granted
        }
    }

}