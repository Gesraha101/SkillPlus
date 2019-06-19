package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.CurrentNeed


data class CurrentNeedResponse(
        var status: Boolean,
        var skills: List<CurrentNeed>,
        var message: String
)