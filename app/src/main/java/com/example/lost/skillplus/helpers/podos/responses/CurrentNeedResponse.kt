package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.CurrentNeed


data class CurrentNeedResponse(
        var status: Boolean,
        var data: List<CurrentNeed>,
        var message: String
)