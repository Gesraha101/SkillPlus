package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Request


data class MyNeedResponse(
        var status: Boolean,
        var sqlresponse: List<Request>,
        var message: String
)