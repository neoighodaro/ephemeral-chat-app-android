package com.neo.chatkitephemeralmessage


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pusher.chatkit.messages.multipart.Message
import com.pusher.chatkit.messages.multipart.Payload
import java.net.URLEncoder


class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    private var messageList = ArrayList<Message>()
    private var recyclerView: RecyclerView? = null
    val chatkitApp = ChatkitApp()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    fun addMessage(message: Message) {
        val index = messageList.indexOfFirst { it.id == message.id }
        if (index >= 0) {
            messageList[index] = message
        } else {
            if (message.sender.id != ChatkitApp.currentUser.id) {
                deleteMessage(message.id)
            }
            messageList.add(message)
            recyclerView?.scrollToPosition(messageList.size - 1)
        }

        // checks if message contains DELETED, remove from list
        when (val data = message.parts[0].payload) {
            is Payload.Inline -> {
                if (data.content.equals("DELETED", false)){
                    messageList.remove(message)
                    notifyDataSetChanged()
                }
            }
        }
        notifyDataSetChanged()
    }

    private fun deleteMessage(messageId: Int) {
        chatkitApp.deleteMessage(messageId)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_list_row, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(messageList[position])

    override fun getItemCount() = messageList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val username: TextView = itemView.findViewById(R.id.editTextUsername)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(item: Message) {

            username.text = item.sender.name
            try {
                image.visibility = View.VISIBLE
                when (val payload = item.parts[1].payload) {
                    is Payload.Attachment -> {
                        payload.url { result ->
                            when (result) {
                                is com.pusher.util.Result.Success -> {
                                    val url = result.value
                                    image.setImageBitmap(chatkitApp.getBitmapFromUrl(url))

                                }
                            }

                        }
                    }
                }

            } catch (e: Exception) {
                image.visibility = View.GONE
            }

        }

    }

}