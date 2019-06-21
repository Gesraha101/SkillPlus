package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.Notification
import java.io.Serializable

data class NotificationsResponse(
        var status : Boolean,
        val notifications: ArrayList<Notification>,
        var message : String): Serializable