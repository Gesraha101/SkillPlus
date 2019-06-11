package com.example.lost.skillplus.models.podos.responses

data class SkillsResponse(
        var status : Boolean,
        var categoriesName: Any?,//Todo : What kind of object is this?
        var message : String
)