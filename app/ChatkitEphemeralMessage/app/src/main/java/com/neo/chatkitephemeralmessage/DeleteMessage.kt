package com.neo.chatkitephemeralmessage

import com.google.gson.annotations.SerializedName

data class DeleteMessage(

    @field:SerializedName("messageId")
    val messageId: Int? = 0,

    @field:SerializedName("timer")
    val timer: Int? = 0
)