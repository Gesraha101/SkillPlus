package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Category

data class SkillsResponse(
        var status : Boolean,
        var categoriesName: Any?,
        var message : String
)