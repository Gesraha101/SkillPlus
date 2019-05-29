package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Category

data class CategoriesResponse(
        var status : Boolean,
        var categories : List<Category>,
        var message : String
)