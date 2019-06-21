package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.Request


data class CurrentNeedDetailsResponse(
        var status: Boolean,
        var skills: List<Request>,
        var message: String
)