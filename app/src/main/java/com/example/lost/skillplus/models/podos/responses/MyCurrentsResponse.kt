package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Request
import com.example.lost.skillplus.models.podos.raw.Skill


data class MyCurrentsResponse(
        var status: Boolean,
        var skills: List<Skill>?,
        var needs : List<Request>?,
        var message: String
)