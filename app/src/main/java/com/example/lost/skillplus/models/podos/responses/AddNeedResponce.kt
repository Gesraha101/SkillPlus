package com.example.lost.skillplus.models.podos.responses

import com.example.lost.skillplus.models.podos.raw.ResponseApplyArray
import java.io.Serializable

class AddNeedResponce (
    var status : Boolean,
    var categoriesName : ResponseApplyArray,
    var message : String
) : Serializable