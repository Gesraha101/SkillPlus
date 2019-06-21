package com.example.lost.skillplus.helpers.podos.responses

data class SkillsResponse(
        var status : Boolean,
        var sqlresponse: SQLResponse?,
        var message : String
)