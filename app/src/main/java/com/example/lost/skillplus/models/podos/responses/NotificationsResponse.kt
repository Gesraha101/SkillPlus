package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Notification
import java.io.Serializable

data class NotificationsResponse(
        var status : Boolean,
        val notifications: List<Notification>,
        var message : String) : Serializable