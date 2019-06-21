package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.Request


data class MyNeedResponse(
        var status: Boolean,
        var sqlresponse: List<Request>,
        var message: String
)