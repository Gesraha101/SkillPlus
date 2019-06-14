package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.Skill


data class FavouriteResponse(
        var status : Boolean,
        var sqlresponse: SQLResponse?,
        var data : List<Skill>?,
        var message : String
)