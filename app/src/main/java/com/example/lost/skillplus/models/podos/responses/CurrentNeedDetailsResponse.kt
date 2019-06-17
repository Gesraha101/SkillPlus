package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Request
import com.example.lost.skillplus.models.podos.raw.Skill


data class CurrentNeedDetailsResponse(
        var status: Boolean,
        var skills: List<Request>,
        var message: String
)