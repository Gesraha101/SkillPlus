package com.example.lost.skillplus.helpers.podos.responses

data class FormResponse(
        var status : Boolean,
        var sqlresponse: SQLResponse?,
        var message : String)