package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.Request
import com.example.lost.skillplus.helpers.podos.raw.Skill


data class MyCurrentsResponse(
        var status: Boolean,
        var skills: List<Skill>?,
        var needs : List<Request>?,
        var message: String
)