package com.example.lost.skillplus.helpers.podos.responses

import com.example.lost.skillplus.helpers.podos.raw.Category

data class CategoriesResponse(
        var status : Boolean,
        var categories : List<Category>,
        var message : String
)