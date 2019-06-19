package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.CurrentNeed


data class CurrentNeedResponse(
        var status: Boolean,
        var data: List<CurrentNeed>,
        var message: String
)