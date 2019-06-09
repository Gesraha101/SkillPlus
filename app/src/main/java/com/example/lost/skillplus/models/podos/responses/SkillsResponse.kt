package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Category

data class SkillsResponse(
        var status : Boolean,
        var categoriesName : List<Category>, //Todo: ask walaa what kind of object is this
        var message : String
)